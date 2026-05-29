package root.git_turl.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.answer.enums.AnswerType;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.converter.ReportConverter;
import root.git_turl.domain.report.dto.GithubRepoResponse;
import root.git_turl.domain.report.dto.ReportReqDto;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.dto.ReportSavedEvent;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.enums.Status;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.Pagination;
import root.git_turl.infrastructure.github.GithubClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final GithubClient githubClient;
    private final ReportAsyncService reportAsyncService;
    private final ReportRepository reportRepository;
    private final QuestionRepository questionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<ReportResDto.RepoInfo> getRepoList(String token) {
        List<GithubRepoResponse> repos = githubClient.getRepos(token);
        return repos.stream()
                .map(repo -> ReportConverter.toRepoRes(repo))
                .toList();
    }

    @Transactional
    public ReportResDto.ReportId postReport(Member currentMember, ReportReqDto.Repo dto) {
        Report report = ReportConverter.toReport(currentMember.getGithubId(), dto.getFullName(), currentMember);
        reportRepository.save(report);
        eventPublisher.publishEvent(new ReportSavedEvent(report.getId(), currentMember.getEmail(), currentMember.getGithubId(), dto));
        return ReportConverter.toReportId(report);
    }

    public ReportResDto.ReportDetail getReportDetail(Member currentMember, Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

        validateOwner(currentMember, report);

        if (!report.getGenerationStatus().equals(GenerationStatus.DONE)) {
            return null;
        }

        return ReportConverter.toReportDetail(report);
    }

    @Transactional
    public ReportResDto.NewStatus updateStatus(Member currentMember, Long reportId, ReportReqDto.NewStatus dto) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

        validateOwner(currentMember, report);

        report.updateStatus(dto.getStatus());
        return ReportConverter.toNewStatus(report.getStatus());
    }

    @Transactional(readOnly = true)
    public ReportResDto.Pagination<ReportResDto.ReportPreview> getReportList(
            Member currentMember,
            Integer pageSize,
            String cursor,
            LocalDate startDate,
            LocalDate endDate,
            Status status,
            AnswerType answerType
    ) {
        if (pageSize == null) pageSize = 10;
        if (cursor == null) cursor = "-1";
        PageRequest pageRequest = PageRequest.of(0, pageSize);

        long idCursor;
        Slice<Report> reportList;
        String nextCursor;
        GenerationStatus done = GenerationStatus.DONE;

        // 기간별 조회
        if (startDate != null && endDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(LocalTime.MAX);
            if (!cursor.equals("-1")) {
                idCursor = Long.parseLong(cursor);
                reportList = reportRepository.findByMember_IdAndGenerationStatusAndIdLessThanAndCreatedAtBetweenOrderByIdDesc(currentMember.getId(), done, idCursor, pageRequest, start, end);
            } else {
                reportList = reportRepository.findByMember_IdAndGenerationStatusAndCreatedAtBetweenOrderByIdDesc(currentMember.getId(), done, pageRequest, start, end);
            }
        } else if (status != null){
            // 상태별 조회
            if (!cursor.equals("-1")) {
                idCursor = Long.parseLong(cursor);
                reportList = reportRepository.findReportByMember_IdAndGenerationStatusAndStatusAndIdLessThanOrderByIdDesc(currentMember.getId(), done, status, idCursor, pageRequest);
            } else {
                reportList = reportRepository.findReportByMember_IdAndGenerationStatusAndStatusOrderByIdDesc(currentMember.getId(), done, status, pageRequest);
            }
        } else {
            // 일반 조회
            if (!cursor.equals("-1")) {
                idCursor = Long.parseLong(cursor);
                reportList = reportRepository.findReportByMember_IdAndGenerationStatusAndIdLessThanOrderByIdDesc(currentMember.getId(), done, idCursor, pageRequest);
            } else {
                reportList = reportRepository.findReportByMember_IdAndGenerationStatusOrderByIdDesc(currentMember.getId(), done, pageRequest);
            }
        }

        if (reportList.getContent().isEmpty()) {
            return null;
        }

        nextCursor = reportList.getContent().getLast().getId().toString();
        List<ReportResDto.ReportPreview> reportPreviewList =
                reportList.stream()
                        .map(report -> {
                            long count = answerType.equals(AnswerType.ALL)
                                    ? questionRepository.countByReportAndStatus(
                                    report,
                                    GenerationStatus.DONE
                            )
                                    : questionRepository.countByReportAndStatusAndAnswerType(
                                    report,
                                    GenerationStatus.DONE,
                                    answerType
                            );

                            return Map.entry(report, count);
                        })
                        .filter(entry -> answerType.equals(AnswerType.ALL)
                                || entry.getValue() > 0)
                        .map(entry ->
                                ReportConverter.toReportPreview(
                                        entry.getKey(),
                                        entry.getValue()
                                )
                        )
                        .toList();

        return Pagination.toPagination(reportPreviewList, reportList.hasNext(), nextCursor, reportList.getContent().size());
    }

    @Transactional
    public ReportResDto.NewTitle updateReportTitle(Member currentMember, ReportReqDto.NewTitle dto, Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

        validateOwner(currentMember, report);

        report.updateTitle(dto.getTitle());
        return ReportConverter.toNewTitle(report);
    }

    private static void validateOwner(Member currentMember, Report report) {
        if (!currentMember.getGithubId().equals(report.getGithubId())) {
            throw new ReportException(ReportErrorCode.NO_AUTH_REPORT);
        }
    }
}
