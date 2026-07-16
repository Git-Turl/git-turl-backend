package root.git_turl.domain.comment.repository;

import root.git_turl.domain.board.enums.BoardType;

import java.time.LocalDateTime;

public interface MyCommentProjection {
    Long getCommentId();
    Long getBoardId();
    String getBoardTitle();
    BoardType getBoardType();
    String getContent();

    // 비밀댓글 여부
    Boolean getIsSecret();

    Long getLikeCount();
    LocalDateTime getCreatedAt();
}