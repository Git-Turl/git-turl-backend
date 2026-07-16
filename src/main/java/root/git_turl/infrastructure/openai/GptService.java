package root.git_turl.infrastructure.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import root.git_turl.domain.answer.dto.Feedback;
import root.git_turl.domain.answer.dto.VoiceFeedback;
import root.git_turl.domain.question.dto.QuestionContent;
import root.git_turl.domain.report.dto.ProblemList;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;

@Service
@RequiredArgsConstructor
public class GptService {
    private final RequestGpt requestGpt;

    public ReportWrapper analyzeGit(String prompt) {
        return requestGpt.requestGpt(prompt, ReportWrapper.class);
    }

    public QuestionContent makeQuestions(String prompt) {
        return requestGpt.requestGpt(prompt, QuestionContent.class);
    }

    public Feedback makeFeedback(String prompt) {
        return requestGpt.requestGpt(prompt, Feedback.class);
    }

    public VoiceFeedback makeVoiceFeedback(String prompt) {
        return requestGpt.requestGpt(prompt, VoiceFeedback.class);
    }

    public ProblemList makeReportProblem(String prompt) {return requestGpt.requestGpt(prompt, ProblemList.class);}


}
