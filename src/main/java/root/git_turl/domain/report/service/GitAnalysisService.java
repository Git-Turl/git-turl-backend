package root.git_turl.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.git_turl.domain.report.dto.commit.CommitTypeCount;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.dto.commit.MajorCommit;
import root.git_turl.global.util.GithubUserMapper;
import root.git_turl.infrastructure.github.GitLogService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitAnalysisService {

    private final GitLogService gitLogService;
    private final GithubUserMapper githubUserMapper;

    public GitAnalysisResult analyze(String repoFullName, List<GitCommit> commits, List<GitCommit> userCommits) {

        int totalCommits = commits.size();
        int userTotalCommits = userCommits.size();

        Map<GitCommit, Integer> scoreMap = new HashMap<>();

        // 타입 분류
        long feat = 0, fix = 0, refactor = 0, etc = 0;

        for (GitCommit commit : commits) {
            int score = 0;
            String msg = commit.getMessage().toLowerCase();

            if (msg.startsWith("feat")) {
                feat++;
                score += 5;
            }
            else if (msg.startsWith("fix")) {
                fix++;
                score += 4;
            }
            else if (msg.startsWith("refactor")) {
                refactor++;
                score += 3;
            }
            else {
                etc++;
                score += 1;
            }

            // 최신 여부
            if (commit.getDate().isAfter(LocalDate.now().minusDays(7))) score += 3;
            else if (commit.getDate().isAfter(LocalDate.now().minusDays(30))) score += 1;

            // 사용자 커밋
            if (userCommits.contains(commit)) score += 3;

            scoreMap.put(commit, score);
        }

        // 상위 커밋
        List<MajorCommit> majorCommits = scoreMap.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(5)
                .map(entry -> {
                    GitCommit commit = entry.getKey();

                    String diff = gitLogService.getCommitDiff(repoFullName, commit.getHash());

                    return MajorCommit.builder()
                            .hash(commit.getHash())
                            .message(commit.getMessage())
                            .diff(diff)
                            .build();
                })
                .toList();

        // 사용자별 커밋
        Map<String, Long> contributionAnalyze = commits.stream()
                .collect(Collectors.groupingBy(
                        c -> githubUserMapper.resolveLogin(c.getAuthorEmail(), repoFullName),
                        Collectors.counting()
                ));

        // 샘플 메시지
        List<String> sampleMessages = userCommits.stream()
                .filter(c -> !c.getMessage().startsWith("Merge"))
                .map(GitCommit::getMessage)
                .limit(30)
                .collect(Collectors.toList());

        return GitAnalysisResult.builder()
                .totalCommits(totalCommits)
                .userTotalCommits(userTotalCommits)
                .contributionRate(Math.round((double) userTotalCommits / totalCommits * 1000) / 10.0)
                .commitTypeCount(CommitTypeCount.builder()
                        .featCount(feat)
                        .fixCount(fix)
                        .refactorCount(refactor)
                        .etcCount(etc)
                        .build())
                .sampleMessages(sampleMessages)
                .majorCommits(majorCommits)
                .contributionAnalyze(contributionAnalyze)
                .build();
    }
}