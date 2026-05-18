package root.git_turl.domain.comment.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,
            "COMMENT404_1",
            "댓글을 찾을 수 없습니다"),

    NO_COMMENT_PERMISSION(HttpStatus.FORBIDDEN,
            "COMMENT403_1",
            "댓글 권한이 없습니다"),

    NO_EDIT(HttpStatus.BAD_REQUEST,
            "COMMENT400_1",
            "수정할 댓글 정보가 없습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
