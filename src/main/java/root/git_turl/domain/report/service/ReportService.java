package root.git_turl.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.converter.ReportConverter;
import root.git_turl.domain.report.dto.GithubRepoResponse;
import root.git_turl.domain.report.dto.ReportReqDto;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.Pagination;
import root.git_turl.infrastructure.github.GithubClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final GithubClient githubClient;
    private final ReportAsyncService reportAsyncService;
    private final ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public List<ReportResDto.RepoInfo> getRepoList(String token) {
        List<GithubRepoResponse> repos = githubClient.getRepos(token);
        return repos.stream()
                .map(repo -> ReportConverter.toRepoRes(repo))
                .toList();
    }

    @Transactional
    public ReportResDto.ReportId postReport(Member currentMember, ReportReqDto.Repo dto) {
        Report report = ReportConverter.toReport(currentMember.getGithubId(), dto.getFullName());
        reportRepository.save(report);
        reportAsyncService.generateReport(report.getId(), currentMember, dto);
        return ReportConverter.toReportId(report);
    }

    public ReportResDto.ReportDetail getReportDetail(Member currentMember, Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

        if (!currentMember.getGithubId().equals(report.getGithubId())) {
            throw new ReportException(ReportErrorCode.NO_AUTH_REPORT);
        }

        return ReportConverter.toReportDetail(report);
    }

    @Transactional
    public ReportResDto.NewStatus updateStatus(Member currentMember, Long reportId, ReportReqDto.NewStatus dto) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

        if (!currentMember.getGithubId().equals(report.getGithubId())) {
            throw new ReportException(ReportErrorCode.NO_AUTH_REPORT);
        }

        report.updateStatus(dto.getStatus());
        return ReportConverter.toNewStatus(report.getStatus());
    }

    public ReportResDto.Pagination<ReportResDto.ReportPreview> getReportList(
            Member currentMember,
            Integer pageSize,
            String cursor,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (pageSize == null) pageSize = 10;
        if (cursor == null) cursor = "-1";
        PageRequest pageRequest = PageRequest.of(0, pageSize);

        long idCursor;
        Slice<Report> reportList;
        String nextCursor;

        // 기간별 조회
        if (startDate != null && endDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(LocalTime.MAX);
            if (!cursor.equals("-1")) {
                idCursor = Long.parseLong(cursor);
                reportList = reportRepository.findByMember_IdAndIdLessThanAndCreatedAtBetweenOrderByIdDesc(currentMember.getId(), idCursor, pageRequest, start, end);
            } else {
                reportList = reportRepository.findByMember_IdAndCreatedAtBetweenOrderByIdDesc(currentMember.getId(), pageRequest, start, end);
            }
        } else {
            // 일반 조회
            if (!cursor.equals("-1")) {
                idCursor = Long.parseLong(cursor);
                reportList = reportRepository.findReportByMember_IdAndIdLessThanOrderByIdDesc(currentMember.getId(), idCursor, pageRequest);
            } else {
                reportList = reportRepository.findReportByMember_IdOrderByIdDesc(currentMember.getId(), pageRequest);
            }
        }

        nextCursor = reportList.getContent().getLast().getId().toString();
        List<ReportResDto.ReportPreview> reportPreviewList =
                reportList.stream()
                        .map(ReportConverter::toReportPreview)
                        .toList();

        return Pagination.toPagination(reportPreviewList, reportList.hasNext(), nextCursor, reportList.getContent().size());
    }
}
