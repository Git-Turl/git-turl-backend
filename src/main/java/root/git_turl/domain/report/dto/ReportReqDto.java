package root.git_turl.domain.report.dto;

import lombok.Getter;
import root.git_turl.domain.report.enums.Status;

public class ReportReqDto {
    @Getter
    public static class Repo {
        private String fullName;
    }

    @Getter
    public static class NewStatus {
        private Status status;
    }
}
