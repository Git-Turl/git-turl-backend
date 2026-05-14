package root.git_turl.domain.question.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class QuestionContent {
    public Map<String, Integer> questions;
}
