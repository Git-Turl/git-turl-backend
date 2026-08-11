package root.git_turl.domain.report.service.stat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import root.git_turl.domain.report.dto.RepresentativeFile;
import root.git_turl.domain.report.dto.stat.FileStat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FileContentReader {

    private static final int MAX_CHARS_PER_FILE = 2500;

    public List<RepresentativeFile> readWithBudget(String repoPath, List<FileStat> fileStats,
                                                   int totalBudget, boolean useUserStats) {
        List<RepresentativeFile> result = new ArrayList<>();
        int used = 0;

        for (FileStat stat : fileStats) {
            String relativePath = stat.getFilePath();
            try {
                Path fullPath = Path.of(repoPath, relativePath);
                if (!Files.exists(fullPath)) continue;

                String content = Files.readString(fullPath);
                boolean truncated = false;

                if (content.length() > MAX_CHARS_PER_FILE) {
                    content = content.substring(0, MAX_CHARS_PER_FILE) + "\n... (이하 생략)";
                    truncated = true;
                }

                if (used + content.length() > totalBudget) {
                    log.info("파일 예산 초과로 중단: {} (사용량={}, 예산={})", relativePath, used, totalBudget);
                    break;
                }

                int commitCount = useUserStats ? stat.getUserCommitCount() : stat.getCommitCount();
                int changedLines = useUserStats ? stat.getUserChangedLines() : stat.getTotalChangedLines();

                result.add(new RepresentativeFile(relativePath, content, truncated, commitCount, changedLines));
                used += content.length();
            } catch (IOException e) {
                log.warn("파일 읽기 실패: {}", relativePath, e);
            }
        }
        return result;
    }
}