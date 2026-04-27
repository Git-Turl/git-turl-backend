package root.git_turl.infrastructure.github;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.report.dto.GithubRepoResponse;

import java.util.*;

@Component
public class GithubClient {

    private final RestClient restClient;
    private static String GITHUB_API = "https://api.github.com";

    public GithubClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public String getEmail(String accessToken) {
        List<Map<String, Object>> response = restClient.get()
                .uri(GITHUB_API + "/user/emails")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        for (Map<String, Object> emailInfo : response) {
            if (Boolean.TRUE.equals(emailInfo.get("primary"))) {
                return (String) emailInfo.get("email");
            }
        }

        return null;
    }

    public List<GithubRepoResponse> getRepos(String accessToken) {

        String query = """
            query {
              viewer {
                repositories(
                    first: 30) {
                  nodes {
                       name
                       nameWithOwner
                       url
                       description
                       isPrivate
                       updatedAt
                  }
                }
                repositoriesContributedTo(
                    first: 30,
                    contributionTypes: [COMMIT, PULL_REQUEST]) {
                  nodes {
                       name
                       nameWithOwner
                       url
                       description
                       isPrivate
                       updatedAt
                  }
                }
              }
            }
        """;

        Map<String, Object> response = restClient.post()
                .uri(GITHUB_API+"/graphql")
                .header("Authorization", "Bearer " + accessToken)
                .body(Map.of("query", query))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        Map<String, Object> viewer = (Map<String, Object>) data.get("viewer");

        List<GithubRepoResponse> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        extractRepos(viewer, "repositories", result, seen);
        extractRepos(viewer, "repositoriesContributedTo", result, seen);

        result.sort((a, b) ->
                b.getUpdatedAt().compareTo(a.getUpdatedAt())
        );

        return result;
    }
    @SuppressWarnings("unchecked")
    private void extractRepos(Map<String, Object> viewer,
                              String field,
                              List<GithubRepoResponse> result,
                              Set<String> seen) {

        Map<String, Object> repoData = (Map<String, Object>) viewer.get(field);
        if (repoData == null) return;

        List<Map<String, Object>> nodes =
                (List<Map<String, Object>>) repoData.get("nodes");

        if (nodes == null) return;

        for (Map<String, Object> node : nodes) {
            String url = (String) node.get("url");

            if (url != null && seen.add(url)) {
                result.add(GithubRepoResponse.builder()
                        .name((String) node.get("name"))
                        .fullName((String) node.get("nameWithOwner"))
                        .description((String) node.get("description"))
                        .isPrivate((boolean) node.get("isPrivate"))
                        .updatedAt((String) node.get("updatedAt"))
                        .build());
            }
        }
    }
}
