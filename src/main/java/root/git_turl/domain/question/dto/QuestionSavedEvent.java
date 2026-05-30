package root.git_turl.domain.question.dto;

import java.util.List;

public record QuestionSavedEvent(
        Long reportId,
        Integer count,
        List<Long> questionIds
) {}