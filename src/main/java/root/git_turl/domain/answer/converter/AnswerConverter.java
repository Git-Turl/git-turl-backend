package root.git_turl.domain.answer.converter;

import root.git_turl.domain.answer.dto.AnswerResDto;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.enums.AnswerType;
import root.git_turl.domain.question.entity.Question;

public class AnswerConverter {

    public static Answer toTextAnswer(
            String content,
            Question question
    ) {
        return Answer.builder()
                .content(content)
                .answerType(AnswerType.TEXT)
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
}
