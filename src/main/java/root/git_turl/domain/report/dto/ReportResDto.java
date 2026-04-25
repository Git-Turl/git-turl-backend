package root.git_turl.domain.report.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.enums.Status;

import java.time.LocalDateTime;


public class ReportResDto {

    @Getter
    @Builder
    public static class RepoInfo {
        private String name;
        private String fullName;
        private String description;
        private boolean isPrivate;
    }

    @Getter
    @Builder
    public static class ReportId {
        private Long reportId;
    }

    @Getter
    @Builder
    public static class ReportDetail {
        private Long reportId;
        private String repoName;
        private String githubId;
        private Status status;
        private LocalDateTime createdAt;
        private ReportWrapper content;
    }
}
