package root.git_turl.domain.question.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import root.git_turl.domain.answer.enums.AnswerType;

public class QuestionReqDto {

    @Getter
    public static class QuestionInfo {
        @NotNull
        @Min(1)
        @Max(5)
        private Integer questionCount;

        @NotNull
        private AnswerType answerType;
    }
}
