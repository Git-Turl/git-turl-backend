package root.git_turl.domain.report.dto.commit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GitCommit {
    private String hash;
    private String authorName;
    private String authorEmail;
    private LocalDate date;
    private String message;
}
