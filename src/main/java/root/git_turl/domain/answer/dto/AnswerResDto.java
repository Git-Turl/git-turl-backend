package root.git_turl.domain.answer.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class AnswerResDto {

    @Getter
    @Builder
    public static class TextAnswer{
        private Long answerId;
        private String content;
        private String feedback;
        private LocalDate createdAt;
    }
}
