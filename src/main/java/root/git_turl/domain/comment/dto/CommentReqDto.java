package root.git_turl.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class CommentReqDto {

    @Data
    public static class CreateCommentReqDto {

        @NotBlank
        private String content;

        private Long parentId;

        private Boolean isSecret;
    }

    @Data
    public static class UpdateCommentReqDto {

        private String content;

        private Boolean isSecret;
    }
}
