package root.git_turl.domain.report.dto;

import lombok.Builder;
import lombok.Getter;


public class ReportResDto {

    @Getter
    @Builder
    public static class RepoInfo {
        private String name;
        private String fullName;
        private String description;
        private boolean isPrivate;
    }
}
