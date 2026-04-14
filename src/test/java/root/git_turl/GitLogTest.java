package root.git_turl;

import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.service.GitAnalysisService;
import root.git_turl.infrastructure.github.GitCloneService;
import root.git_turl.global.util.GitLogParser;
import root.git_turl.infrastructure.github.GitLogService;

import java.util.List;

public class GitLogTest {

    public static void main(String[] args) {

        GitCloneService cloneService = new GitCloneService();
        GitLogParser parser = new GitLogParser();
        GitAnalysisService gitAnalyzer = new GitAnalysisService(new GitLogService());

        String gitUrl = "https://github.com/UMC-TODAY/today-server.git";
        String githubName = "seoyoon lee";

        String repoPath = cloneService.cloneRepository(gitUrl);

        List<GitCommit> commits = parser.getCommits(repoPath);
        List<GitCommit> userCommits = commits.stream()
                .filter(c -> c.getAuthor().equals(githubName))
                .toList();

        GitAnalysisResult result = gitAnalyzer.analyze(repoPath, commits, userCommits);


        System.out.println("=== repo commit analysis report ===");
        System.out.println("total commit: " + result.getTotalCommits());
        System.out.println("my commit: " + result.getUserTotalCommits() + "(" + result.getContributionRate() + "%)");
        System.out.println("feat: " + result.getCommitTypeCount().getFeatCount());
        System.out.println("fix: " + result.getCommitTypeCount().getFixCount());
        System.out.println("refactor: " + result.getCommitTypeCount().getRefactorCount());

        System.out.println("\nsample messages:");
        result.getSampleMessages().forEach(System.out::println);

        System.out.println("\ndiffs: ");
        result.getMajorCommits().forEach(System.out::println);
    }
}