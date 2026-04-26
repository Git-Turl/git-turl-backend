package root.git_turl.domain.report.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.code.ReportSuccessCode;
import root.git_turl.domain.report.dto.ReportReqDto;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.service.ReportService;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Report", description = "요약본 관련 API")
public class ReportController implements ReportControllerDocs{

    private final ReportService reportService;

    @GetMapping("/repos")
    public ApiResponse<List<ReportResDto.RepoInfo>> getRepos(
            @CurrentUser @Parameter(hidden = true) Member member
    ) {
        String accessToken = member.getGithubAccessToken();
        return ApiResponse.onSuccess(ReportSuccessCode.REPO_LIST_GET_OK, reportService.getRepoList(accessToken));
    }

    @PostMapping("/reports")
    public ApiResponse<ReportResDto.ReportId> postReport(
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestBody ReportReqDto.Repo dto
            ) {
        return ApiResponse.onSuccess(ReportSuccessCode.REPORT_POST_OK, reportService.postReport(member, dto));
    }

    @GetMapping("/reports/{reportId}")
    public ApiResponse<ReportResDto.ReportDetail> getReport(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long reportId
    ) {
        ReportResDto.ReportDetail response = reportService.getReportDetail(member, reportId);
        if (response == null) {
            return ApiResponse.onSuccess(ReportSuccessCode.REPORT_PROCESSING, response);
        }
        return ApiResponse.onSuccess(ReportSuccessCode.REPORT_GET_OK, response);
    }

    @PatchMapping("/reports/{reportId}/status")
    public ApiResponse<ReportResDto.NewStatus> updateStatus(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long reportId,
            @RequestBody ReportReqDto.NewStatus dto
    ) {
        return ApiResponse.onSuccess(ReportSuccessCode.REPORT_STATUS_PATCH_OK, reportService.updateStatus(member, reportId, dto));
    }

    @GetMapping("/reports")
    public ApiResponse<ReportResDto.Pagination<ReportResDto.ReportPreview>> getReports(
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        return ApiResponse.onSuccess(ReportSuccessCode.REPORT_LIST_GET_OK, reportService.getReportList(member, pageSize, cursor, startDate, endDate));
    }

    @PatchMapping("/reports/{reportId}/title")
    public ApiResponse<ReportResDto.NewTitle> updateReportTitle(
            @CurrentUser @Parameter(hidden = true) Member member,
            @Valid @RequestBody ReportReqDto.NewTitle dto,
            @RequestParam Long reportId
    ) {
        return ApiResponse.onSuccess(ReportSuccessCode.REPORT_TITLE_PATCH_OK, reportService.updateReportTitle(member, dto, reportId));
    }

}
