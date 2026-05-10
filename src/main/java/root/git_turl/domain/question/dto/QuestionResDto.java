package root.git_turl.domain.question.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.report.enums.GenerationStatus;

import java.time.LocalDate;
import java.util.List;

public class QuestionResDto {

    @Getter
    @Builder
    public static class QuestionInfo{
        private Long questionId;
        private String content;
        private LocalDate createdAt;
        private GenerationStatus status;
        private Integer time;
    }

    @Getter
    @Builder
    public static class QuestionId{
        private List<Long> questionIdList;
    }
}
