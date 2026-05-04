package root.git_turl.domain.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.question.converter.QuestionConverter;
import root.git_turl.domain.question.dto.QuestionContent;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsyncQuestionService {

    private final BuildPrompt buildPrompt;
    private final ReportRepository reportRepository;
    private final GptService gptService;
    private final QuestionRepository questionRepository;

    @Async
    @Transactional
    public void makeQuestion(Long reportId, Integer count, List<Long> questionIdLists) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));
        List<Question> questions =
                questionRepository.findAllById(questionIdLists);
        try {
            String prompt = buildPrompt.buildQuestionPrompt(report, count);
            List<String> contents = gptService.makeQuestions(prompt).getQuestions();

            for (int i=0; i<questions.size(); i++) {
                questions.get(i).updateContent(contents.get(i));
                questions.get(i).updateStatus(GenerationStatus.DONE);
            }
        } catch (Exception e) {
            for (int i=0; i<count; i++) {
                questions.get(i).updateStatus(GenerationStatus.FAIL);
            }
        }
    }
}
