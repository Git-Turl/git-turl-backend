package root.git_turl.domain.report.dto.stat;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class FileStat {
    private String filePath;
    private int totalChangedLines = 0;
    private int commitCount = 0;
    private int userChangedLines = 0;
    private int userCommitCount = 0;
    private LocalDate lastModified;

    public FileStat(String filePath) {
        this.filePath = filePath;
    }

    public void addChange(int addedPlusDeleted, boolean isUserCommit, LocalDate commitDate) {
        totalChangedLines += addedPlusDeleted;
        commitCount++;
        if (isUserCommit) {
            userChangedLines += addedPlusDeleted;
            userCommitCount++;
        }
        if (lastModified == null || commitDate.isAfter(lastModified)) {
            lastModified = commitDate;
        }
    }

    public String getFilePath() {
        return filePath;
    }
}
