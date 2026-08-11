package root.git_turl.domain.report.dto;

public record RepresentativeFile(
        String filePath,
        String content,
        boolean truncated,
        int commitCount,
        int changedLines
) {}