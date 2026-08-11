package root.git_turl.domain.report.dto.commit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class MajorCommit {
    private String hash;
    private String message;
    private String diff;
}
