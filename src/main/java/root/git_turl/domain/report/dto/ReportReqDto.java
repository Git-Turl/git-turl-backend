package root.git_turl.domain.report.dto;

import lombok.Getter;

public class ReportReqDto {
    @Getter
    public static class Repo {
        private String fullName;
    }
}
