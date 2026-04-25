package root.git_turl.domain.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.converter.ReportConverter;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.GithubRepoResponse;
import root.git_turl.domain.report.dto.ReportReqDto;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.dto.reportDetail.ReportContent;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.BuildPrompt;
import root.git_turl.global.util.GitLogParser;
import root.git_turl.global.util.GitRepoParser;
import root.git_turl.infrastructure.github.GitCloneService;
import root.git_turl.infrastructure.github.GithubClient;
import root.git_turl.infrastructure.openai.GptService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final GithubClient githubClient;
    private final GitCloneService gitCloneService;
    private final GitLogParser gitLogParser;
    private final GitAnalysisService gitAnalysisService;
    private final BuildPrompt buildPrompt;
    private final GptService gptService;
    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    public List<ReportResDto.RepoInfo> getRepoList(String token) {
        List<GithubRepoResponse> repos = githubClient.getRepos(token);
        return repos.stream()
                .map(repo -> ReportConverter.toRepoRes(repo))
                .toList();
    }

    public ReportResDto.ReportId postReport(Member currentMember, ReportReqDto.Repo dto) {
        String gitUrl = GitRepoParser.getRepoLink(dto.getFullName());

        String email = currentMember.getEmail();

        String repoPath = gitCloneService.cloneRepository(gitUrl);

        List<GitCommit> commits = gitLogParser.getCommits(repoPath);
        List<GitCommit> userCommits = commits.stream()
                .filter(c -> c.getAuthorEmail().equals(email) || c.getAuthorEmail().contains(currentMember.getGithubId()))
                        .toList();

        GitAnalysisResult result = gitAnalysisService.analyze(GitRepoParser.getRepoFullName(gitUrl), repoPath, commits, userCommits);

        String prompt = buildPrompt.buildReportPrompt(result);
        ReportWrapper content = gptService.analyzeGit(prompt);
        if (content == null) {
            throw new ReportException(ReportErrorCode.GPT_RESPONSE_NOT_FOUND);
        }
        String contentJson;
        try {
            contentJson = objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }

        Report report = ReportConverter.toReport(contentJson, currentMember.getGithubId());
        reportRepository.save(report);
        return ReportConverter.toReportId(report);
    }
}
