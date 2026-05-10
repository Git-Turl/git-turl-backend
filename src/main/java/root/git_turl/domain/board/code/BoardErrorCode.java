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
            "파일 업로드에 실패했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
