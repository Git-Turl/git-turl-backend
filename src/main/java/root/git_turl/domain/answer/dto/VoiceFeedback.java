package root.git_turl.domain.answer.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class VoiceFeedback {
    private String content;
    private String answerSummary;
    private List<String> keywords;
}
