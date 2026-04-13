package root.git_turl.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.util.List;


public interface ReportControllerDocs {

    @Operation(
            summary = "레포지토리 목록 조회",
            description = "사용자의 레포지토리 목록을 조회합니다."
    )
    public ApiResponse<List<ReportResDto.RepoInfo>> getRepos(
            @CurrentUser @Parameter(hidden = true) Member member
    );
}
