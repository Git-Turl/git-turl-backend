package root.git_turl.domain.report.service.stat;

import org.springframework.stereotype.Component;
import root.git_turl.domain.report.dto.stat.FileStat;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class FileSelector {

    public List<FileStat> selectProjectFiles(Map<String, FileStat> fileStats, int limit) {
        return fileStats.values().stream()
                .filter(this::isAnalyzable)
                .sorted(Comparator.comparingInt(this::projectScore).reversed())
                .limit(limit)
                .toList();
    }

    public List<FileStat> selectUserFiles(Map<String, FileStat> fileStats, int limit) {
        return fileStats.values().stream()
                .filter(this::isAnalyzable)
                .filter(s -> s.getUserChangedLines() > 0)
                .sorted(Comparator.comparingInt(this::userScore).reversed())
                .limit(limit)
                .toList();
    }

    private boolean isAnalyzable(FileStat s) {
        String path = s.getFilePath();
        return path.endsWith(".java") || path.endsWith(".ts") || path.endsWith(".js")
                || path.endsWith(".py") || path.endsWith(".kt");
    }

    private int projectScore(FileStat s) {
        int score = 0;
        score += Math.min(s.getTotalChangedLines() / 20, 10);
        score += Math.min(s.getCommitCount(), 5);
        score += pathWeight(s.getFilePath());
        return score;
    }

    private int userScore(FileStat s) {
        int score = 0;
        score += Math.min(s.getUserChangedLines() / 15, 10);
        score += Math.min(s.getUserCommitCount(), 5);
        double ratio = (double) s.getUserChangedLines() / Math.max(s.getTotalChangedLines(), 1);
        if (ratio > 0.5) score += 3;
        score += pathWeight(s.getFilePath());
        return score;
    }

    private int pathWeight(String path) {
        if (path.contains("/service/") || path.contains("/domain/")) return 3;
        if (path.contains("/controller/")) return 2;
        if (path.contains("/entity/") || path.contains("/repository/")) return 2;
        if (path.contains("/config/") || path.contains("/test/")) return -2;
        return 0;
    }
}