package root.git_turl.domain.report.dto.gpt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class GptRequest {
    private String model = "gpt-4o-mini";
    private List<GptMessage> messages;
}