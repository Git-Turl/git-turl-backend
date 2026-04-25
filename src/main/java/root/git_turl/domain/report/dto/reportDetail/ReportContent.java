package root.git_turl.domain.report.dto.reportDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportContent {
    private String purpose;
    private Stack stack;
    private CommitStats commitStats;
    private Map<String, Long> commitContribution;
    private Scale scale;
    private String reports;
}
