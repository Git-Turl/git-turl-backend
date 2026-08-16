package root.git_turl.domain.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import root.git_turl.domain.report.dto.RepresentativeFile;
import root.git_turl.domain.report.dto.commit.CommitTypeCount;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.dto.stat.FileStat;
import root.git_turl.domain.report.service.stat.FileContentReader;
import root.git_turl.domain.report.service.stat.FileSelector;
import root.git_turl.domain.report.service.stat.FileStatAggregator;
import root.git_turl.global.util.GithubUserMapper;
import root.git_turl.global.util.parser.ReadmeParser;
import root.git_turl.infrastructure.github.GitLogService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitAnalysisService {

    private static final int MAX_PROJECT_FILES = 10;
    private static final int MAX_USER_FILES = 8;
    private static final int PROJECT_FILE_BUDGET = 40_000; // chars
    private static final int USER_FILE_BUDGET = 30_000;    // chars

    private final GithubUserMapper githubUserMapper;
    private final FileStatAggregator fileStatAggregator;
    private final FileSelector fileSelector;
    private final FileContentReader fileContentReader;
    private final GitLogService gitLogService;

    public GitAnalysisResult analyze(String repoFullName, String repoPath,
                                     List<GitCommit> commits, List<GitCommit> userCommits) {

        Set<String> userHashes = userCommits.stream()
                .map(GitCommit::getHash)
                .collect(Collectors.toSet());

        // 1) 파일 단위 통계 집계 (churn, commit 횟수, 유저 기여량 등)
        Map<String, FileStat> fileStats = fileStatAggregator.aggregate(repoPath, commits, userHashes);

        // 2) 프로젝트 대표 파일 / 유저 대표 파일 분리 선정
        // 2) 프로젝트 대표 파일 / 유저 대표 파일 분리 선정 (FileStat 리스트로 선정)
        List<FileStat> projectFileStats = fileSelector.selectProjectFiles(fileStats, MAX_PROJECT_FILES);
        List<FileStat> userFileStats = fileSelector.selectUserFiles(fileStats, MAX_USER_FILES);

        // 3) 실제 파일 내용 읽기 + 토큰 예산 내 축약
        List<RepresentativeFile> projectFiles =
                fileContentReader.readWithBudget(repoPath, projectFileStats, PROJECT_FILE_BUDGET, false);
        List<RepresentativeFile> userFiles =
                fileContentReader.readWithBudget(repoPath, userFileStats, USER_FILE_BUDGET, true);

        // 4) 커밋 타입 분류 (정량 지표 - 커밋 기반 유지)
        long feat = 0, fix = 0, refactor = 0, etc = 0;
        for (GitCommit commit : commits) {
            String msg = commit.getMessage().toLowerCase();
            if (msg.startsWith("feat")) feat++;
            else if (msg.startsWith("fix")) fix++;
            else if (msg.startsWith("refactor")) refactor++;
            else etc++;
        }

        int totalCommits = commits.size();
        int userTotalCommits = userCommits.size();

        // 5) README
        String readme = ReadmeParser.readReadme(repoPath);

        // 6) 유저별 기여 분포
        Map<String, Long> contributionAnalyze = commits.stream()
                .collect(Collectors.groupingBy(
                        c -> githubUserMapper.resolveLogin(c.getAuthorEmail(), repoFullName),
                        Collectors.counting()
                ));

        // 7) 커밋 메시지 샘플 (기여 분석 보조 자료로 유지 - 파일 원문만으로 부족한 맥락 보완)
        List<String> sampleMessages = userCommits.stream()
                .filter(c -> !c.getMessage().startsWith("Merge"))
                .map(GitCommit::getMessage)
                .limit(30)
                .toList();

        int totalFileCount = gitLogService.countCurrentFiles(repoPath);

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
                .totalFileCount(totalFileCount)
                .sampleMessages(sampleMessages)
                .contributionAnalyze(contributionAnalyze)
                .readmeSummary(readme)
                .projectRepresentativeFiles(projectFiles)
                .userRepresentativeFiles(userFiles)
                .build();
    }
}