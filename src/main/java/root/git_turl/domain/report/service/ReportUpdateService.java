package root.git_turl.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.repository.ReportRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportUpdateService {

    private final ReportRepository reportRepository;

    @Transactional
    public void updateReport(
            Long reportId,
            String contentJson,
            String description,
            GenerationStatus status
    ) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow();

        report.updateContent(contentJson);
        report.updateDescription(description);
        report.updateGenerationStatus(status);
    }

    @Transactional
    public void fail(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow();

        report.updateGenerationStatus(GenerationStatus.FAIL);
    }

    @Transactional
    public void updateGenerationStatus(Long reportId, GenerationStatus status) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow();
        report.updateGenerationStatus(GenerationStatus.FAIL);
    }
}