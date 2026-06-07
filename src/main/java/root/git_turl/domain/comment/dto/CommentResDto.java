package root.git_turl.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.board.enums.BoardType;

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
        private Boolean isDeleted;
        private String content;
        private String writerName;
        private String profileImage;
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

    @Getter
    @Builder
    public static class MyCommentPreviewDto {
        private Long commentId;
        private Long boardId;
        private String boardTitle;
        private BoardType boardType;
        private String content;
        private Long likeCount;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class MyCommentPreviewListDto {
        private List<MyCommentPreviewDto> commentList;
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Getter
    @Builder
    public static class CommentLikeResultDto {
        private Boolean isLiked;
        private Long likeCount;
    }
}