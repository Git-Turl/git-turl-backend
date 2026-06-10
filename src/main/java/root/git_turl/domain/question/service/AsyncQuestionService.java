package root.git_turl.domain.question.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import root.git_turl.domain.question.dto.QuestionSavedEvent;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.question.exception.QuestionException;
import root.git_turl.domain.question.exception.code.QuestionErrorCode;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.prompt.BuildJudgePrompt;
import root.git_turl.global.util.prompt.BuildPrompt;
import root.git_turl.global.util.prompt.BuildRetryPrompt;
import root.git_turl.infrastructure.judge.JudgeResult;
import root.git_turl.infrastructure.judge.JudgeService;
import root.git_turl.infrastructure.judge.Result;
import root.git_turl.infrastructure.openai.GptService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncQuestionService {

    private final BuildPrompt buildPrompt;
    private final ReportRepository reportRepository;
    private final GptService gptService;
    private final QuestionRepository questionRepository;
    private final BuildJudgePrompt buildJudgePrompt;
    private final JudgeService judgeService;
    private final BuildRetryPrompt buildRetryPrompt;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void makeQuestion(QuestionSavedEvent event) {
        log.info("async 시작");

        Report report = reportRepository.findById(event.reportId())
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));
        List<Question> questions =
                questionRepository.findAllById(event.questionIds());
        log.info(
                "makeQuestion start event={}, thread={}",
                event.questionIds(),
                Thread.currentThread().getName()
        );

        log.info("questionList={}", questions.stream().map(q -> q.getId()).toList());
        try {
            String prompt = buildPrompt.buildQuestionPrompt(report, event.count());
            Map<String,Integer> contents = gptService.makeQuestions(prompt).getQuestions();
            List<String> keys = new ArrayList<>(contents.keySet());
            log.info("questionIdList={}", event.questionIds());

            for (int i = 0; i < questions.size(); i++) {
                // LLM-as-a-judge
                String judgePrompt = buildJudgePrompt.buildQuestionJudgePrompt(keys.get(i));
                JudgeResult judgeResult = judgeService.evaluate(judgePrompt);
                log.info("질문 내용: {}", keys.get(i));
                log.info("평가 점수: {}", judgeResult.score());
                log.info("평가 결과: {}", judgeResult.result());
                log.info("평가 이유: {}", judgeResult.reason());

                String finalKey;
                Integer finalTime;

                if (judgeResult.result() == Result.FAIL) {
                    String retryPrompt = buildRetryPrompt.buildQuestionRetryPrompt(report, event.count(), judgeResult.reason());
                    Map<String, Integer> retryContents = gptService.makeQuestions(retryPrompt).getQuestions();
                    List<String> retryKeys = new ArrayList<>(retryContents.keySet());

                    String retryJudgePrompt = buildJudgePrompt.buildQuestionJudgePrompt(retryKeys.get(i));
                    JudgeResult retryJudgeResult = judgeService.evaluate(retryJudgePrompt);
                    log.info("재시도 점수: {}", retryJudgeResult.score());
                    log.info("재시도 결과: {}", retryJudgeResult.result());
                    log.info("재시도 이유: {}", retryJudgeResult.reason());

                    if (retryJudgeResult.result() == Result.FAIL) {
                        throw new QuestionException(QuestionErrorCode.QUESTION_GENERATION_FAIL);
                    }

                    finalKey = retryKeys.get(i);
                    finalTime = retryContents.get(finalKey);
                } else {
                    finalKey = keys.get(i);
                    finalTime = contents.get(finalKey);
                }

                questions.get(i).updateContent(finalKey);
                questions.get(i).updateTime(finalTime);
                questions.get(i).updateStatus(GenerationStatus.DONE);
            }
            questionRepository.saveAll(questions);
            log.info("update 완료");
        } catch (Exception e) {
            log.error("비동기 실패", e);
            for (int i=0; i<event.count(); i++) {
                questions.get(i).updateStatus(GenerationStatus.FAIL);
            }
        }
    }
}
