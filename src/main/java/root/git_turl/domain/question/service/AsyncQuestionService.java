package root.git_turl.domain.question.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import root.git_turl.domain.question.converter.QuestionConverter;
import root.git_turl.domain.question.dto.QuestionContent;
import root.git_turl.domain.question.dto.QuestionSavedEvent;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.question.exception.QuestionException;
import root.git_turl.domain.question.exception.code.QuestionErrorCode;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.BuildPrompt;
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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void makeQuestion(QuestionSavedEvent event) {
        log.info("async 시작");

        Report report = reportRepository.findById(event.reportId())
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));
        List<Question> questions =
                questionRepository.findAllById(event.questionIds());
        log.info("questionList={}", questions.stream().map(q -> q.getId()).toList());
        try {
            String prompt = buildPrompt.buildQuestionPrompt(report, event.count());
            log.info("prompt 생성 완료");
            Map<String,Integer> contents = gptService.makeQuestions(prompt).getQuestions();
            log.info("gpt raw response={}", contents);
            List<String> keys = new ArrayList<>(contents.keySet());
            log.info("questionIdList={}", event.questionIds());

            for (int i=0; i<questions.size(); i++) {
                questions.get(i).updateContent(keys.get(i));
                questions.get(i).updateTime(contents.get(keys.get(i)));
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
