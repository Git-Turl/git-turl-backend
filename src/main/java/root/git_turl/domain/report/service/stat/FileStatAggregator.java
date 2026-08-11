package root.git_turl.domain.report.service.stat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.dto.stat.FileStat;
import root.git_turl.infrastructure.github.GitLogService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class FileStatAggregator {

    private final GitLogService gitLogService;

    public Map<String, FileStat> aggregate(String repoPath, List<GitCommit> commits, Set<String> userHashes) {
        Map<String, FileStat> statMap = new ConcurrentHashMap<>();

        for (GitCommit commit : commits) {
            boolean isUserCommit = userHashes.contains(commit.getHash());
            String numstat = gitLogService.getNumstat(repoPath, commit.getHash());
            if (numstat == null || numstat.isBlank()) continue;

            for (String line : numstat.split("\n")) {
                String[] parts = line.split("\t");
                if (parts.length < 3) continue;

                int added = parseIntSafe(parts[0]);
                int deleted = parseIntSafe(parts[1]);
                String filePath = parts[2];

                if (added < 0 || deleted < 0) continue;

                FileStat stat = statMap.computeIfAbsent(filePath, FileStat::new);
                stat.addChange(added + deleted, isUserCommit, commit.getDate());
            }
        }
        return statMap;
    }

    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1; // "-" (binary file) 등
        }
    }
}
