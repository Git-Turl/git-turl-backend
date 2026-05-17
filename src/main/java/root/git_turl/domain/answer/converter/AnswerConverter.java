package root.git_turl.domain.answer.converter;

import root.git_turl.domain.answer.dto.AnswerResDto;
import root.git_turl.domain.answer.dto.VoiceFeedback;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.enums.AnswerType;
import root.git_turl.domain.answer.enums.Status;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.enums.GenerationStatus;

import java.util.List;

public class AnswerConverter {

    public static Answer toTextAnswer(
            String content,
            Question question
    ) {
        return Answer.builder()
                .content(content)
                .answerType(AnswerType.TEXT)
                .generationStatus(GenerationStatus.DONE)
                .question(question)
                .build();
    }

    public static AnswerResDto.TextAnswer toTextAnswer(
            Answer answer
    ) {
        return AnswerResDto.TextAnswer.builder()
                .answerId(answer.getId())
                .content(answer.getContent())
                .feedback(answer.getFeedback())
                .createdAt(answer.getCreatedAt().toLocalDate())
                .build();
    }

    public static Answer toVoiceAnswer(
            String voiceFileUrl,
            Question question
    ) {
        return Answer.builder()
                .answerType(AnswerType.VOICE)
                .voiceFile(voiceFileUrl)
                .generationStatus(GenerationStatus.PROCESSING)
                .question(question)
                .build();
    }

    public static Answer toVoiceAnswerPass(
            Question question
    ) {
        return Answer.builder()
                .answerType(AnswerType.VOICE)
                .generationStatus(GenerationStatus.DONE)
                .status(Status.PASSED)
                .question(question)
                .build();
    }

    public static AnswerResDto.VoiceAnswer toVoiceAnswerDto(
           Answer answer,
           List<String> keywords
    ) {
        return AnswerResDto.VoiceAnswer.builder()
                .answerId(answer.getId())
                .content(answer.getContent())
                .feedback(answer.getFeedback())
                .createdAt(answer.getCreatedAt().toLocalDate())
                .answerSummary(answer.getAnswerSummary())
                .keywords(keywords)
                .voiceFile(answer.getVoiceFile())
                .status(answer.getStatus())
                .build();
    }
}
