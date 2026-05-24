package root.git_turl.domain.report.dto;

public record ReportSavedEvent (
    Long reportId,
    String email,
    String githubId,
    ReportReqDto.Repo dto
) {}
