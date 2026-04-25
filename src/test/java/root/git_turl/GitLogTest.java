package root.git_turl;

import org.springframework.web.client.RestClient;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.service.GitAnalysisService;
import root.git_turl.global.util.BuildPrompt;
import root.git_turl.global.util.GitRepoParser;
import root.git_turl.global.util.GithubUserMapper;
import root.git_turl.infrastructure.github.GitCloneService;
import root.git_turl.global.util.GitLogParser;
import root.git_turl.infrastructure.github.GitLogService;
import root.git_turl.infrastructure.github.GithubClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GitLogTest {

    public static void main(String[] args) {

        GitCloneService cloneService = new GitCloneService();
        GitLogParser parser = new GitLogParser(new GithubUserMapper(RestClient.builder().build()));
        GitAnalysisService gitAnalyzer = new GitAnalysisService(new GitLogService(), new GithubUserMapper(RestClient.builder().build()));
        BuildPrompt buildPrompt = new BuildPrompt();
        GithubClient githubClient = new GithubClient(RestClient.builder());

        String gitUrl = GitRepoParser.getRepoLink("UMC-TODAY/today-server");

        String githubName = "";
        String email = "leeseoyoon01@gmail.com";

        String repoPath = cloneService.cloneRepository(gitUrl);

        List<GitCommit> commits = parser.getCommits(repoPath);
        List<GitCommit> userCommits = commits.stream()
                .filter(c -> c.getAuthorEmail().equals(email) || c.getAuthorEmail().contains("seoyoon127") || c.getAuthorName().equals(githubName))
                .toList();
        System.out.println(userCommits.size());


        GitAnalysisResult result = gitAnalyzer.analyze(GitRepoParser.getRepoFullName(gitUrl), repoPath, commits, userCommits);

        System.out.println("=== repo commit analysis report ===");
        System.out.println("total commit: " + result.getTotalCommits());
        System.out.println("my commit: " + result.getUserTotalCommits() + "(" + result.getContributionRate() + "%)");
        System.out.println("feat: " + result.getCommitTypeCount().getFeatCount());
        System.out.println("fix: " + result.getCommitTypeCount().getFixCount());
        System.out.println("refactor: " + result.getCommitTypeCount().getRefactorCount());
        System.out.println("contribution: " + result.getContributionAnalyze());

            System.out.println("\nsample messages:");
            result.getSampleMessages().forEach(System.out::println);

            System.out.println("\ndiffs: ");
            result.getMajorCommits().forEach(System.out::println);

        String prompt = buildPrompt.buildReportPrompt(result);

        try {
            Files.writeString(
                    Path.of("prompt.txt"),
                    prompt,
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}