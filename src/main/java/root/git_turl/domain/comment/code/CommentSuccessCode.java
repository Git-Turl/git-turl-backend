package root.git_turl.domain.comment.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum CommentSuccessCode implements BaseSuccessCode {

    COMMENT_CREATED(HttpStatus.CREATED,
            "COMMENT201_1",
            "댓글 생성에 성공했습니다."),

    COMMENT_UPDATED(HttpStatus.OK,
            "COMMENT200_1",
            "댓글 수정에 성공했습니다."),

    COMMENT_DELETED(HttpStatus.OK,
            "COMMENT200_2",
            "댓글 삭제에 성공했습니다."),

    COMMENT_LIST_FOUND(HttpStatus.OK,
            "COMMENT200_3",
            "댓글 목록 조회에 성공했습니다."),

    COMMENT_LIKE_CREATED(
            HttpStatus.CREATED,
            "COMMENT201_2",
            "댓글 좋아요에 성공했습니다."
    ),

    COMMENT_LIKE_DELETED(
            HttpStatus.OK,
            "COMMENT200_4",
            "댓글 좋아요 취소에 성공했습니다."
    ),;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
