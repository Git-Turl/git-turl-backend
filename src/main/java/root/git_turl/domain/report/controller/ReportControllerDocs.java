package root.git_turl.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.dto.ReportReqDto;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.enums.Status;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.time.LocalDate;
import java.util.List;


public interface ReportControllerDocs {

    @Operation(
            summary = "레포지토리 목록 조회",
            description = "사용자의 레포지토리 목록을 조회합니다."
    )
    public ApiResponse<List<ReportResDto.RepoInfo>> getRepos(
            @CurrentUser @Parameter(hidden = true) Member member
    );

    @Operation(
            summary = "레포 분석본 생성",
            description = "해당 레포지토리 분석본을 생성합니다."
    )
    public ApiResponse<ReportResDto.ReportId> postReport(
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestBody ReportReqDto.Repo dto
    );

    @Operation(
            summary = "분석본 상세 보기",
            description = "해당 id의 분석본을 상세 조회합니다."
    )
    public ApiResponse<ReportResDto.ReportDetail> getReport(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long reportId
    );

    @Operation(
            summary = "분석본 공개 설정 변경",
            description = "해당 id의 분석본 공개 여부를 변경합니다."
    )
    public ApiResponse<ReportResDto.NewStatus> updateStatus(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long reportId,
            @RequestBody ReportReqDto.NewStatus dto
    );

    @Operation(
            summary = "분석본 목록 조회",
            description = "사용자의 분석본 목록을 조회합니다. (커서 기반 페이징)"
    )
    public ApiResponse<ReportResDto.Pagination<ReportResDto.ReportPreview>> getReports(
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Status status
    );

    @Operation(
            summary = "분석본 제목 변경",
            description = "해당 id의 분석본 제목을 변경합니다."
    )
    public ApiResponse<ReportResDto.NewTitle> updateReportTitle(
            @CurrentUser @Parameter(hidden = true) Member member,
            @Valid @RequestBody ReportReqDto.NewTitle dto,
            @RequestParam Long reportId
    );
}
