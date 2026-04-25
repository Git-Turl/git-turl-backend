package root.git_turl.domain.report.converter;

import root.git_turl.domain.report.dto.GithubRepoResponse;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.Status;

public class ReportConverter {

    public static ReportResDto.RepoInfo toRepoRes(
            GithubRepoResponse repos
    ) {
        return ReportResDto.RepoInfo.builder()
                .name(repos.getName())
                .fullName(repos.getFullName())
                .description(repos.getDescription())
                .isPrivate(repos.isPrivate())
                .build();
    }

    public static Report toReport(
            String contentJson,
            String githubId
    ) {
        return Report.builder()
                .contentJson(contentJson)
                .githubId(githubId)
                .status(Status.PRIVATE)
                .build();
    }

    public static ReportResDto.ReportId toReportId(
            Report report
    ) {
        return ReportResDto.ReportId.builder()
                .reportId(report.getId())
                .build();
    }
}
