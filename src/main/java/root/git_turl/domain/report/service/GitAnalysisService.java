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

    public GitAnalysisResult analyze(String repoFullName, String repoPath, List<GitCommit> commits, List<GitCommit> userCommits) {

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

        List<GitCommit> candidates = scoreMap.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(30)
                .map(Map.Entry::getKey)
                .toList();

        Map<GitCommit, Integer> finalScoreMap = new HashMap<>();
        Map<String, String> diffCache = new HashMap<>();

        // diff 기반 점수 추가
        for (GitCommit commit : candidates) {
            int score = scoreMap.get(commit);

            String diff = diffCache.computeIfAbsent(
                    commit.getHash(),
                    h -> gitLogService.getCommitDiff(repoPath, h)
            );

            int diffScore = calculateDiffScore(diff);

            finalScoreMap.put(commit, score + diffScore);
        }

        // 상위 커밋
        List<MajorCommit> majorCommits = finalScoreMap.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(5)
                .map(entry -> {
                    GitCommit commit = entry.getKey();
                    String diff = diffCache.get(commit.getHash());

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

    // diff 점수 계산
    private int calculateDiffScore(String diff) {
        if (diff == null || diff.isBlank()) return 0;

        int added = 0;
        int deleted = 0;
        int files = 0;

        for (String line : diff.split("\n")) {
            if (line.startsWith("diff --git")) files++;
            else if (line.startsWith("+") && !line.startsWith("+++")) added++;
            else if (line.startsWith("-") && !line.startsWith("---")) deleted++;
        }

        int score = 0;

        // 변경 규모
        int total = added + deleted;
        if (total > 500) score += 5;
        else if (total > 200) score += 3;
        else if (total > 50) score += 1;

        // 파일 수
        if (files > 10) score += 3;
        else if (files > 5) score += 2;

        // 추가, 삭제 균형
        if (added > 0 && deleted > 0) score += 2;

        return score;
    }
}