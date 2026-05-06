package root.git_turl.domain.question.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class QuestionReqDto {

    @Getter
    public static class QuestionCount {
        @NotNull
        @Min(1)
        @Max(5)
        private Integer questionCount;
    }
}
