package root.git_turl.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProblemList {
    private List<Problem> problems;
}
