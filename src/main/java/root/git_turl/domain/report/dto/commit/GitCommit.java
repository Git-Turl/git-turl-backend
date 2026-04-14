package root.git_turl.domain.report.dto.commit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GitCommit {
    private String hash;
    private String author;
    private LocalDate date;
    private String message;
}
