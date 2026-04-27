package root.git_turl.domain.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.ReportReqDto;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.BuildPrompt;
import root.git_turl.global.util.GitLogParser;
import root.git_turl.global.util.GitRepoParser;
import root.git_turl.infrastructure.github.GitCloneService;
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

    @Async
    @Transactional
    public void generateReport(Long reportId, Member currentMember, ReportReqDto.Repo dto) {
        log.info("비동기 실행됨: {}", Thread.currentThread().getName());
        String email = currentMember.getEmail();
        String gitUrl = GitRepoParser.getRepoLink(dto.getFullName());
        String repoPath = gitCloneService.cloneRepository(gitUrl);

        Report report = reportRepository.findById(reportId)
                .orElseThrow();

        report.updateGenerationStatus(GenerationStatus.PROCESSING);
        reportRepository.save(report);

        try {
            List<GitCommit> commits = gitLogParser.getCommits(repoPath);
            List<GitCommit> userCommits = commits.stream()
                    .filter(c -> c.getAuthorEmail().equals(email) || c.getAuthorEmail().contains(currentMember.getGithubId()))
                    .toList();

            GitAnalysisResult result = gitAnalysisService.analyze(GitRepoParser.getRepoFullName(gitUrl), repoPath, commits, userCommits);

            String prompt = buildPrompt.buildReportPrompt(result);
            ReportWrapper content = gptService.analyzeGit(prompt);
            if (content.getContent() == null) {
                throw new ReportException(ReportErrorCode.GPT_RESPONSE_NOT_FOUND);
            }
            String contentJson;
            try {
                contentJson = objectMapper.writeValueAsString(content);
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
}
