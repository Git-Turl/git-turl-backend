package root.git_turl.domain.answer.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.answer.enums.Status;

import java.time.LocalDate;
import java.util.List;

public class AnswerResDto {

    @Getter
    @Builder
    public static class TextAnswer{
        private Long answerId;
        private String content;
        private String feedback;
        private LocalDate createdAt;
    }

    @Getter
    @Builder
    public static class VoiceAnswer{
        private Long answerId;
        private String content;
        private String feedback;
        private LocalDate createdAt;
        private String voiceFile;
        private String answerSummary;
        private List<String> keywords;
        private Status status;
    }

    @Getter
    @Builder
    public static class TranscriptionResponse {
        private String text;
    }
}
