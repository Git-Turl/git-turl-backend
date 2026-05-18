package root.git_turl.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResDto {

    @Getter
    @Builder
    public static class CommentCreateResultDto {
        private Long commentId;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class CommentUpdateResultDto {
        private Long commentId;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class CommentDeleteResultDto {
        private Long commentId;
        private LocalDateTime deletedAt;
    }

    @Getter
    @Builder
    public static class CommentPreviewDto {

        private Long commentId;
        private Long parentId;
        private Integer depth;
        private Boolean isSecret;
        private String content;
        private String writerName;
        private Long likeCount;
        private Boolean isLiked;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class CommentPreviewListDto {
        private List<CommentPreviewDto> commentList;
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}