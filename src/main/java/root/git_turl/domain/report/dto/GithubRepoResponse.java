package root.git_turl.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GithubRepoResponse {
    private String name;
    private String fullName;
    private String description;
    private boolean isPrivate;
    private String updatedAt;
}
