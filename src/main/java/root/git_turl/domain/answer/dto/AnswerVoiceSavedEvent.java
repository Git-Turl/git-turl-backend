package root.git_turl.domain.answer.dto;

public record AnswerVoiceSavedEvent(
        String questionContent,
        Integer questionTime,
        String voiceFileUrl,
        Long answerId
) {}
