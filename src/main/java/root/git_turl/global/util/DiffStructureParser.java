package root.git_turl.global.util;

import lombok.Builder;
import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiffStructureParser {

    private static final Pattern DIFF_HEADER = Pattern.compile("^diff --git a/(.+?) b/(.+?)$");

    public DiffSummary parse(String diff) {
        if (diff == null || diff.isBlank()) {
            return DiffSummary.builder()
                    .fileCount(0)
                    .addedLines(0)
                    .deletedLines(0)
                    .changedFiles(Collections.emptyList())
                    .build();
        }

        int fileCount = 0;
        int addedLines = 0;
        int deletedLines = 0;
        List<ChangedFile> changedFiles = new ArrayList<>();

        for (String line : diff.split("\\R")) {
            line = line.trim();

            // "4 files changed, 24 insertions(+)" 같은 줄
            if (line.contains("files changed")) {
                Matcher m = Pattern.compile("(\\d+) files? changed(?:, (\\d+) insertions?\\(\\+\\))?(?:, (\\d+) deletions?\\(-\\))?")
                        .matcher(line);
                if (m.find()) {
                    fileCount = Integer.parseInt(m.group(1));
                    if (m.group(2) != null) addedLines = Integer.parseInt(m.group(2));
                    if (m.group(3) != null) deletedLines = Integer.parseInt(m.group(3));
                }
            }

            // 파일별 stats 라인
            if (line.matches(".*\\|\\s*\\d+\\s*\\+.*")) {
                String fileName = line.split("\\|")[0].trim();
                changedFiles.add(ChangedFile.builder()
                        .fileName(fileName)
                        .addedLines(0)
                        .deletedLines(0)
                        .build());
            }
        }

        return DiffSummary.builder()
                .fileCount(fileCount)
                .addedLines(addedLines)
                .deletedLines(deletedLines)
                .changedFiles(changedFiles)
                .build();
    }

    private static class FileChangeCounter {
        int added = 0;
        int deleted = 0;
    }

    @Getter
    @Builder
    public static class DiffSummary {
        private final int fileCount;
        private final int addedLines;
        private final int deletedLines;
        private final List<ChangedFile> changedFiles;
    }

    @Getter
    @Builder
    public static class ChangedFile {
        private final String fileName;
        private final int addedLines;
        private final int deletedLines;
    }
}