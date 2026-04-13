package root.git_turl.domain.report.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.code.ReportSuccessCode;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.service.ReportService;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

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
}
