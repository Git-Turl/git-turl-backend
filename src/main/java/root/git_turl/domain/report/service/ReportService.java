package root.git_turl.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.git_turl.domain.report.converter.ReportConverter;
import root.git_turl.domain.report.dto.GithubRepoResponse;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.infrastructure.github.GithubClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final GithubClient githubClient;

    public List<ReportResDto.RepoInfo> getRepoList(String token) {
        List<GithubRepoResponse> repos = githubClient.getRepos(token);
        return repos.stream()
                .map(repo -> ReportConverter.toRepoRes(repo))
                .toList();
    }
}
