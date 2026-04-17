package root.git_turl.global.util;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class GithubUserMapper {

    private final RestClient restClient;
    private final String GITHUB_API = "https://api.github.com";

    private final Map<String, String> emailToLoginMap = new HashMap<>();

    public GithubUserMapper(RestClient restClient) {
        this.restClient = restClient;
    }

    public String resolveLogin(String email, String repoFullName) {

        if (email == null) return null;

        String login = extractLogin(email);
        if (login != null) return login;

        if (emailToLoginMap.containsKey(email)) {
            return emailToLoginMap.get(email);
        }

        login = fetchLoginFromGithub(email, repoFullName);

        if (login != null) {
            emailToLoginMap.put(email, login);
        }

        return login;
    }

    private String fetchLoginFromGithub(String targetEmail, String repoFullName) {

        int page = 1;

        while (true) {
            List<Map<String, Object>> commits = restClient.get()
                    .uri(GITHUB_API + "/repos/" + repoFullName +
                            "/commits?per_page=100&page=" + page)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (commits == null || commits.isEmpty()) break;

            for (Map<String, Object> c : commits) {

                // commit 내부 email
                Map<String, Object> commit = (Map<String, Object>) c.get("commit");
                if (commit == null) continue;

                Map<String, Object> authorInfo = (Map<String, Object>) commit.get("author");

                String email = (String) authorInfo.get("email");

                if (!targetEmail.equals(email)) continue;

                // GitHub login
                Map<String, Object> author = (Map<String, Object>) c.get("author");

                if (author != null) {
                    return (String) author.get("login");
                }
            }

            page++;
        }

        return null;
    }

    private String extractLogin(String email) {
        if (email.endsWith("@users.noreply.github.com") && email.contains("+")) {
            try {
                return email.split("\\+")[1].split("@")[0];
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
