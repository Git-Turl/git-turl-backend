package root.git_turl.domain.report.converter;

import root.git_turl.domain.report.dto.GithubRepoResponse;
import root.git_turl.domain.report.dto.ReportResDto;

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
}
