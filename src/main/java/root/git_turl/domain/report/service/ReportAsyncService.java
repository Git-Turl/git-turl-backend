package root.git_turl.domain.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.ReportReqDto;
import root.git_turl.domain.report.dto.ReportSavedEvent;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.*;
import root.git_turl.infrastructure.github.GitCloneService;
import root.git_turl.infrastructure.judge.JudgeResult;
import root.git_turl.infrastructure.judge.JudgeService;
import root.git_turl.infrastructure.judge.Result;
import root.git_turl.infrastructure.openai.GptService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportAsyncService {

    private final GitLogParser gitLogParser;
    private final GitAnalysisService gitAnalysisService;
    private final BuildPrompt buildPrompt;
    private final GptService gptService;
    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;
    private final GitCloneService gitCloneService;
    private final JudgeService judgeService;
    private final BuildJudgePrompt buildJudgePrompt;
    private final BuildRetryPrompt buildRetryPrompt;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateReport(ReportSavedEvent event) {
        log.info("비동기 실행됨: {}", Thread.currentThread().getName());
        String email = event.email();
        String gitUrl = GitRepoParser.getRepoLink(event.dto().getFullName());
        String repoPath = gitCloneService.cloneRepository(gitUrl);
        Report report = reportRepository.findById(event.reportId())
                .orElseThrow();

        try {
            List<GitCommit> commits = gitLogParser.getCommits(repoPath);
            List<GitCommit> userCommits = commits.stream()
                    .filter(c -> c.getAuthorEmail().equals(email) || c.getAuthorEmail().contains(event.githubId()))
                    .toList();

            GitAnalysisResult result = gitAnalysisService.analyze(GitRepoParser.getRepoFullName(gitUrl), repoPath, commits, userCommits);

            String prompt = buildPrompt.buildReportPrompt(result, event.githubId());
            ReportWrapper content = getContent(prompt);
            String contentJson;

            try {
                contentJson = objectMapper.writeValueAsString(content);
                // LLM-as-a-Judge
                String judgePrompt = buildJudgePrompt.buildReportJudgePrompt(result, contentJson);
                JudgeResult judgeResult = judgeService.evaluate(judgePrompt);
                log.info("분석 요약본: {}",contentJson);
                log.info("평가 점수: {}",judgeResult.score());
                log.info("평가 결과: {}",judgeResult.result());
                log.info("평가 이유: {}",judgeResult.reason());

                // 실패 시 한 번 더 시도
                if (judgeResult.result().equals(Result.FAIL)) {
                    String retryPrompt = buildRetryPrompt.buildReportRetryPrompt(
                            result,
                            event.githubId(),
                            judgeResult.score(),
                            judgeResult.reason()
                    );
                    ReportWrapper retryContent = getContent(retryPrompt);

                    String retryContentJson = objectMapper.writeValueAsString(retryContent);
                    String retryJudgePrompt = buildJudgePrompt.buildReportJudgePrompt(result, retryContentJson);
                    JudgeResult retryResult = judgeService.evaluate(retryJudgePrompt);
                    log.info("분석 요약본: {}",retryContentJson);
                    log.info("평가 점수: {}",retryResult.score());
                    log.info("평가 결과: {}",retryResult.result());
                    log.info("평가 이유: {}",retryResult.reason());

                    if (retryResult.result() == Result.FAIL) {
                        report.updateGenerationStatus(GenerationStatus.FAIL);
                        throw new ReportException(ReportErrorCode.REPORT_GENERATION_FAIL);
                    }

                    content = retryContent;
                    contentJson = retryContentJson;
                }

                report.updateContent(contentJson);
                String description = content.getContent().getPurpose();
                report.updateDescription(description);
                report.updateGenerationStatus(GenerationStatus.DONE);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 변환 실패", e);
            }
        } catch (Exception e) {
            report.updateGenerationStatus(GenerationStatus.FAIL);
        }
    }

    private ReportWrapper getContent(String prompt) {
        ReportWrapper content = gptService.analyzeGit(prompt);
        if (content.getContent() == null) {
            throw new ReportException(ReportErrorCode.GPT_RESPONSE_NOT_FOUND);
        }
        return content;
    }
}
