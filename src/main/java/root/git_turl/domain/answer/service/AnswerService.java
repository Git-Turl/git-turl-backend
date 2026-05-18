package root.git_turl.domain.answer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.answer.converter.AnswerConverter;
import root.git_turl.domain.answer.dto.AnswerReqDto;
import root.git_turl.domain.answer.dto.AnswerResDto;
import root.git_turl.domain.answer.dto.Feedback;
import root.git_turl.domain.answer.dto.VoiceFeedback;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.enums.AnswerType;
import root.git_turl.domain.answer.enums.Status;
import root.git_turl.domain.answer.exception.AnswerException;
import root.git_turl.domain.answer.exception.code.AnswerErrorCode;
import root.git_turl.domain.answer.repository.AnswerRepository;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.question.exception.QuestionException;
import root.git_turl.domain.question.exception.code.QuestionErrorCode;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.global.apiPayload.exception.GeneralException;
import root.git_turl.global.aws.AwsFileService;
import root.git_turl.global.util.BuildPrompt;
import root.git_turl.infrastructure.openai.GptService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final BuildPrompt buildPrompt;
    private final GptService gptService;
    private final AsyncAnswerService asyncAnswerService;
    private final AwsFileService awsFileService;

    @Transactional(readOnly = true)
    public List<AnswerResDto.TextAnswer> getAnswerList(Member currentMember, Long questionId) {
        Question question = getQuestionWithAnswers(questionId);
        validateAuthor(currentMember, question);

        return question.getAnswerList().stream().map(AnswerConverter::toTextAnswer).toList();
    }

    @Transactional
    public void saveAnswer(Member currentMember, Long questionId, String content) {
        Question question = getQuestion(questionId);
        validateAuthor(currentMember, question);

        if (answerRepository.countByQuestion(question) == 3) {
            throw new AnswerException(AnswerErrorCode.ANSWER_OVER_RAGE);
        }

        Answer answer = AnswerConverter.toTextAnswer(content, question);
        answerRepository.save(answer);
    }

    @Transactional
    public void makeFeedback(Member currentMember, Long answerId) {
        Answer answer = getAnswer(answerId);
        validateAuthor(currentMember, answer.getQuestion());

        if (answer.getFeedback() != null) {
            throw new AnswerException(AnswerErrorCode.FEEDBACK_ALREADY_EXISTS);
        }

        String prompt = buildPrompt.buildFeedbackPrompt(answer.getContent(), answer.getQuestion());
        Feedback feedback = gptService.makeFeedback(prompt);
        answer.updateFeedback(feedback.getContent());
    }

    @Transactional
    public void deleteAnswer(Member currentMember, Long answerId) {
        Answer answer = getAnswer(answerId);
        validateAuthor(currentMember, answer.getQuestion());

        if (answer.getVoiceFile() != null) {
            awsFileService.deleteFile(answer.getVoiceFile());
        }

        answerRepository.deleteById(answerId);
    }

    @Transactional(readOnly = true)
    public AnswerResDto.VoiceAnswer getVoiceAnswer(Member currentMember, Long questionId) {
        Question question = getQuestionWithAnswers(questionId);
        validateAuthor(currentMember, question);

        Answer voiceAnswer = question.getAnswerList().stream()
                .findFirst()
                .orElseThrow(() -> new AnswerException(AnswerErrorCode.NOT_FOUND));

        ObjectMapper objectMapper = new ObjectMapper();

        List<String> keywords = null;
        try {
            keywords = objectMapper.readValue(
                    voiceAnswer.getKeyword(),
                    new TypeReference<List<String>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return AnswerConverter.toVoiceAnswerDto(voiceAnswer, keywords);
    }
    
    @Transactional
    public void saveAnswerVoice(Member currentMember, Long questionId, MultipartFile file) {
        Question question = getQuestion(questionId);
        validateAuthor(currentMember, question);

        boolean exists = answerRepository
                .existsByQuestionAndAnswerType(question, AnswerType.VOICE);

        if (exists) {
            throw new AnswerException(AnswerErrorCode.ANSWER_OVER_RAGE);
        }

        // S3에 음성 녹음본 저장
        String voiceFileUrl;
        try {
            voiceFileUrl = awsFileService.uploadVoiceFile(file, currentMember.getId(), questionId);
        } catch (IOException e) {
            throw new MemberException(AnswerErrorCode.VOICE_UPLOAD_FAIL);
        }
        Answer answer = AnswerConverter.toVoiceAnswer(voiceFileUrl, question);
        answerRepository.save(answer);

        asyncAnswerService.saveAnswerVoice(question.getContent(), question.getTime(), voiceFileUrl, answer.getId());
    }

    @Transactional
    public void savePass(Member currentMember, Long questionId) {
        Question question = getQuestion(questionId);
        validateAuthor(currentMember, question);

        Answer answer = AnswerConverter.toVoiceAnswerPass(question);
        answerRepository.save(answer);
    }

    private Question getQuestionWithAnswers(Long questionId) {
        return questionRepository.findQuestionWithAnswer(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.NOT_FOUND));
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.NOT_FOUND));
    }

    private Answer getAnswer(Long answerId) {
        return answerRepository.findByIdWithQuestion(answerId)
                .orElseThrow(() -> new AnswerException(AnswerErrorCode.NOT_FOUND));
    }

    private static void validateAuthor(Member currentMember, Question question) {
        if (!question.getMember().getId().equals(currentMember.getId())) {
            throw new GeneralException(QuestionErrorCode.FORBIDDEN);
        }
    }
}
