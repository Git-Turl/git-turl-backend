package root.git_turl.domain.answer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import root.git_turl.domain.answer.dto.AnswerVoiceSavedEvent;
import root.git_turl.domain.answer.dto.VoiceFeedback;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.exception.AnswerException;
import root.git_turl.domain.answer.exception.code.AnswerErrorCode;
import root.git_turl.domain.answer.repository.AnswerRepository;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.global.util.BuildPrompt;
import root.git_turl.infrastructure.openai.GptService;
import root.git_turl.infrastructure.openai.WhisperService;

@Service
@RequiredArgsConstructor
public class AsyncAnswerService {

    private final AnswerRepository answerRepository;
    private final BuildPrompt buildPrompt;
    private final GptService gptService;
    private final WhisperService whisperService;
    private final ObjectMapper objectMapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAnswerVoice(AnswerVoiceSavedEvent event) {

        Answer answer = answerRepository.findById(event.answerId())
                .orElseThrow(() -> new AnswerException(AnswerErrorCode.NOT_FOUND));

        // 음성 -> 텍스트 변환
        String textAnswer = whisperService.transcribe(event.voiceFileUrl(), answer);

        // 답변 피드백 생성
        String prompt = buildPrompt.buildSummaryAndFeedbackPrompt(textAnswer, event.questionContent(), event.questionTime());
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
