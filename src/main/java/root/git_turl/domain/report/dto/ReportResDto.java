package root.git_turl.domain.report.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.enums.Status;

import java.time.LocalDateTime;
import java.util.List;


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

    @Getter
    @Builder
    public static class NewStatus {
        private Status status;
    }

    @Getter
    @Builder
    public static class Pagination<T> {
        private List<T> data;
        private String nextCursor;
        private boolean hasNext;
        private Integer pageSize;
    }

    @Getter
    @Builder
    public static class ReportPreview {
        private Long reportId;
        private String reopName;
        private String description;
        private LocalDateTime createdAt;
    }
}
