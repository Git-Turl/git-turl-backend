package root.git_turl.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class AnswerReqDto {

    @Getter
    public static class Answer{
        @NotBlank
        @Size(min = 1, max = 200)
        private String content;
    }
}
