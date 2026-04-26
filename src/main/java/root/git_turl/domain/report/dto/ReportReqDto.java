package root.git_turl.domain.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Getter
    public static class NewTitle {
        @NotNull
        @NotBlank
        private String title;
    }
}
