package root.git_turl.infrastructure.judge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import root.git_turl.domain.answer.dto.Feedback;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.exception.AnswerException;
import root.git_turl.domain.answer.exception.code.AnswerErrorCode;
import root.git_turl.global.util.prompt.BuildJudgePrompt;
import root.git_turl.global.util.prompt.BuildRetryPrompt;
import root.git_turl.infrastructure.openai.GptService;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedbackJudge {
    private final BuildJudgePrompt buildJudgePrompt;
    private final JudgeService judgeService;
    private final BuildRetryPrompt buildRetryPrompt;
    private final GptService gptService;

    public String judgeAndGetFeedback(String answerContent, String questionContent, int questionTime, String feedback) {
        String feedbackContent = feedback;
        // LLM-as-a-judge
        String judgePrompt = buildJudgePrompt.buildFeedbackPrompt(
                answerContent,
                questionContent,
                feedback
        );
        JudgeResult judgeResult = judgeService.evaluate(judgePrompt);
        log.info("평가 점수: {}", judgeResult.score());
        log.info("평가 결과: {}", judgeResult.result());
        log.info("평가 이유: {}", judgeResult.reason());

        if (judgeResult.result() == Result.FAIL) {
            // 실패 시 한 번 더
            String retryPrompt = buildRetryPrompt.buildFeedbackRetryPrompt(
                    answerContent,
                    questionContent,
                    questionTime,
                    feedback,
                    judgeResult.reason()
            );
            Feedback retryFeedback = gptService.makeFeedback(retryPrompt);
            String retryJudgePrompt = buildJudgePrompt.buildFeedbackPrompt(
                    answerContent,
                    questionContent,
                    retryFeedback.getContent()
            );
            JudgeResult retryJudgeResult = judgeService.evaluate(retryJudgePrompt);
            log.info("재시도 점수: {}", retryJudgeResult.score());
            log.info("재시도 결과: {}", retryJudgeResult.result());
            log.info("재시도 이유: {}", retryJudgeResult.reason());

            if (retryJudgeResult.result() == Result.FAIL) {
                throw new AnswerException(AnswerErrorCode.FEEDBACK_GENERATION_FAIL);
            }

            feedbackContent = retryFeedback.getContent();
        }
        return feedbackContent;
    }
}
