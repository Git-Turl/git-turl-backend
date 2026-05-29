package root.git_turl.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Problem {
    private String file;
    private String issue;
    private String evidence;
}
