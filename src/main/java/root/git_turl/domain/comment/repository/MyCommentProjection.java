package root.git_turl.domain.comment.repository;

import root.git_turl.domain.board.enums.BoardType;

import java.time.LocalDateTime;

public interface MyCommentProjection {
    Long getCommentId();
    Long getBoardId();
    String getBoardTitle();
    BoardType getBoardType();
    String getContent();
    Long getLikeCount();
    LocalDateTime getCreatedAt();
}