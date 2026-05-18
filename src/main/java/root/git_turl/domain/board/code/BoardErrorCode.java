package root.git_turl.domain.board.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements BaseErrorCode {

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND,
            "BOARD404_1",
            "게시글을 찾을 수 없습니다"),

    NO_BOARD_PERMISSION(HttpStatus.FORBIDDEN,
            "BOARD403_1",
            "게시글 권한이 없습니다"),

    IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,
            "BOARD500_1",
            "파일 업로드에 실패했습니다"),

    BOARD_BAD_REQUEST(
            HttpStatus.BAD_REQUEST,
            "BOARD400_1",
            "잘못된 게시글 요청입니다"
    ),

    EMPTY_TITLE(
            HttpStatus.BAD_REQUEST,
            "BOARD400_2",
            "게시글 제목은 필수입니다"
    ),

    EMPTY_CONTENT(
            HttpStatus.BAD_REQUEST,
            "BOARD400_3",
            "게시글 내용은 필수입니다"
    ),

    NO_EDIT(
            HttpStatus.BAD_REQUEST,
            "BOARD400_4",
            "수정할 게시글 정보가 없습니다"
    );;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
