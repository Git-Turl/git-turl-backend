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
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.ProblemList;
import root.git_turl.domain.report.dto.ReportSavedEvent;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.dto.reportDetail.CommitStats;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.parser.GitLogParser;
import root.git_turl.global.util.parser.GitRepoParser;
import root.git_turl.global.util.prompt.BuildJudgePrompt;
import root.git_turl.global.util.prompt.BuildProblemPrompt;
import root.git_turl.global.util.prompt.BuildPrompt;
import root.git_turl.global.util.prompt.BuildRetryPrompt;
import root.git_turl.infrastructure.github.GitCloneService;
import root.git_turl.infrastructure.judge.JudgeResult;
import root.git_turl.infrastructure.judge.JudgeService;
import root.git_turl.infrastructure.judge.Result;
import root.git_turl.infrastructure.openai.GptService;

import java.time.LocalDateTime;
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
    private final BuildProblemPrompt buildProblemPrompt;
    private final ReportUpdateService reportUpdateService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void generateReport(ReportSavedEvent event) {
        log.info("비동기 실행됨: {}", LocalDateTime.now());
        log.info(
                "generateReport START reportId={}, thread={}",
                event.reportId(),
                Thread.currentThread().getName()
        );
        String email = event.email();
        String gitUrl = GitRepoParser.getRepoLink(event.dto().getFullName());
        log.info("1. clone 시작");
        String repoPath = gitCloneService.cloneRepository(gitUrl);
        log.info("1. clone 완료");

        try {
            log.info("2. git log 시작");
            List<GitCommit> commits = gitLogParser.getCommits(repoPath);
            log.info("2. git log 완료");
            List<GitCommit> userCommits = commits.stream()
                    .filter(c -> c.getAuthorEmail().equals(email) || c.getAuthorEmail().contains(event.githubId()))
                    .toList();
            log.info("3. analyze 시작");
            GitAnalysisResult result = gitAnalysisService.analyze(GitRepoParser.getRepoFullName(gitUrl), repoPath, commits, userCommits);
            log.info("3. analyze 완료");

            String problemPrompt = buildProblemPrompt.buildReportProblemPrompt(result);

            log.info("4. 문제 추출 시작");
            ProblemList extractedProblems = gptService.makeReportProblem(problemPrompt);
            log.info("4. 문제 추출 완료");

            log.info("5. GPT 분석 시작");
            String prompt = buildPrompt.buildReportPrompt(result, event.githubId(), extractedProblems);
            log.info("5. GPT 분석 완료");

            ReportWrapper content = getContent(prompt);
            content.getContent().setCommitStats(
                    new CommitStats(
                            result.getTotalCommits(),
                            result.getUserTotalCommits(),
                            result.getContributionRate()
                    )
            );
            String contentJson;

            try {
                contentJson = objectMapper.writeValueAsString(content);

                log.info("6. LLM 분석 프롬프트 생성");
                // LLM-as-a-Judge
                String judgePrompt = buildJudgePrompt.buildReportJudgePrompt(result, contentJson);
                log.info("judgeprompt={}", judgePrompt);
                log.info("7. Judge 시작");
                JudgeResult judgeResult = judgeService.evaluate(judgePrompt);
                log.info("분석 요약본: {}",contentJson);
                log.info("평가 점수: {}",judgeResult.score());
                log.info("평가 결과: {}",judgeResult.result());
                log.info("평가 이유: {}",judgeResult.reason());

                // 실패 시 한 번 더 시도
                if (judgeResult.result().equals(Result.FAIL)) {
                    log.info("7-2. GPT 분석 재시도");
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
                        reportUpdateService.updateGenerationStatus(
                                event.reportId(),
                                GenerationStatus.FAIL
                        );
                        throw new ReportException(ReportErrorCode.REPORT_GENERATION_FAIL);
                    }

                    content = retryContent;
                    contentJson = retryContentJson;
                }

                String description = content.getContent().getPurpose();
                reportUpdateService.updateReport(
                        event.reportId(),
                        contentJson,
                        description,
                        GenerationStatus.DONE
                );
                log.info("리포트 저장 완료: {}", LocalDateTime.now());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 변환 실패", e);
            }
        } catch (Exception e) {
            log.error("리포트 생성 실패", e);
            reportUpdateService.updateGenerationStatus(
                    event.reportId(),
                    GenerationStatus.FAIL
            );
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
