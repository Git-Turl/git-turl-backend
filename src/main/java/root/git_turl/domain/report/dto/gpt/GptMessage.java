package root.git_turl.domain.report.dto.gpt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GptMessage {
    private String role;
    private String content;
}
