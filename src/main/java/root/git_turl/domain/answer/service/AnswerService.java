package root.git_turl.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.answer.converter.AnswerConverter;
import root.git_turl.domain.answer.dto.AnswerResDto;
import root.git_turl.domain.answer.dto.Feedback;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.exception.AnswerException;
import root.git_turl.domain.answer.exception.code.AnswerErrorCode;
import root.git_turl.domain.answer.repository.AnswerRepository;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.question.exception.QuestionException;
import root.git_turl.domain.question.exception.code.QuestionErrorCode;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.global.apiPayload.exception.GeneralException;
import root.git_turl.global.util.BuildPrompt;
import root.git_turl.infrastructure.openai.GptService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final BuildPrompt buildPrompt;
    private final GptService gptService;

    public List<AnswerResDto.TextAnswer> getAnswerList(Member currentMember, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.NOT_FOUND));

        validateAuthor(currentMember, question);

        return question.getAnswerList().stream().map(AnswerConverter::toTextAnswer).toList();
    }

    @Transactional
    public void saveAnswer(Member currentMember, Long questionId, String content) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.NOT_FOUND));

        validateAuthor(currentMember, question);

        if (answerRepository.countByQuestion(question) == 3) {
            throw new AnswerException(AnswerErrorCode.ANSWER_OVER_RAGE);
        }

        Answer answer = AnswerConverter.toTextAnswer(content, question);
        answerRepository.save(answer);
    }

    @Transactional
    public void makeFeedback(Member currentMember, Long answerId) {
        Answer answer = answerRepository.findByIdWithQuestion(answerId)
                .orElseThrow(() -> new AnswerException(AnswerErrorCode.NOT_FOUND));

        validateAuthor(currentMember, answer.getQuestion());

        if (answer.getFeedback() != null) {
            throw new AnswerException(AnswerErrorCode.FEEDBACK_ALREADY_EXISTS);
        }

        String prompt = buildPrompt.buildFeedbackPrompt(answer.getContent(), answer.getQuestion());
        Feedback feedback = gptService.makeFeedback(prompt);
        answer.updateFeedback(feedback.getContent());
    }

    private static void validateAuthor(Member currentMember, Question question) {
        if (!question.getMember().getId().equals(currentMember.getId())) {
            throw new GeneralException(QuestionErrorCode.FORBIDDEN);
        }
    }
}
