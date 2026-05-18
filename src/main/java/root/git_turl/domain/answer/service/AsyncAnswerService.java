package root.git_turl.domain.answer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.answer.dto.VoiceFeedback;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.exception.AnswerException;
import root.git_turl.domain.answer.exception.code.AnswerErrorCode;
import root.git_turl.domain.answer.repository.AnswerRepository;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.global.util.BuildPrompt;
import root.git_turl.infrastructure.openai.GptService;
import root.git_turl.infrastructure.openai.WhisperService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AsyncAnswerService {

    private final AnswerRepository answerRepository;
    private final BuildPrompt buildPrompt;
    private final GptService gptService;
    private final WhisperService whisperService;
    private final ObjectMapper objectMapper;

    @Async
    @Transactional
    public void saveAnswerVoice(
            String questionContent, Integer questionTime,
            String voiceFileUrl, Long answerId
    ) {

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(AnswerErrorCode.NOT_FOUND));

        // 음성 -> 텍스트 변환
        String textAnswer = whisperService.transcribe(voiceFileUrl, answer);

        // 답변 피드백 생성
        String prompt = buildPrompt.buildSummaryAndFeedbackPrompt(textAnswer, questionContent, questionTime);
        VoiceFeedback response = gptService.makeVoiceFeedback(prompt);

        // 음성 답변, 피드백 저장
        String keywords = null;
        try {
            keywords = objectMapper.writeValueAsString(response.getKeywords());
        } catch (JsonProcessingException e) {
            answer.updateGenerationStatus(GenerationStatus.FAIL);
            throw new RuntimeException(e);
        }
        answer.updateVoiceAnswer(
                textAnswer,
                response.getContent(),
                response.getAnswerSummary(),
                keywords);
    }
}
