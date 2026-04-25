package root.git_turl.domain.report.service;

import lombok.RequiredArgsConstructor;
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
import root.git_turl.infrastructure.github.GitCloneService;
import root.git_turl.infrastructure.github.GithubClient;

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
}
