package root.git_turl.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.answer.converter.AnswerConverter;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.exception.AnswerException;
import root.git_turl.domain.answer.exception.code.AnswerErrorCode;
import root.git_turl.domain.answer.repository.AnswerRepository;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.question.exception.QuestionException;
import root.git_turl.domain.question.exception.code.QuestionErrorCode;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.global.apiPayload.code.BaseErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public void saveAnswer(Member currentMember, Long questionId, String content) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.NOT_FOUND));

        if (!question.getMember().getId().equals(currentMember.getId())) {
            throw new GeneralException(QuestionErrorCode.FORBIDDEN);
        }

        if (answerRepository.countByQuestion(question) == 3) {
            throw new AnswerException(AnswerErrorCode.ANSWER_OVER_RAGE);
        }

        Answer answer = AnswerConverter.toTextAnswer(content, question);
        answerRepository.save(answer);
    }
}
