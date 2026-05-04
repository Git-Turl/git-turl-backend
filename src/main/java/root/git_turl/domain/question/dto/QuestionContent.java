package root.git_turl.domain.question.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionContent {
    public List<String> questions;
}
