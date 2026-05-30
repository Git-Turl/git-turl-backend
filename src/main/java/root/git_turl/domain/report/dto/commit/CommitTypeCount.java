package root.git_turl.domain.report.dto.commit;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommitTypeCount {
    private long featCount;
    private long fixCount;
    private long refactorCount;
    private long etcCount;
}
