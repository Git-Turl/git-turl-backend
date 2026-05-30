package root.git_turl.domain.report.dto.reportDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stack {
    private String language;
    private String framework;
    private String library;
    private String security;
}