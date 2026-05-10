package root.git_turl.domain.board.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum BoardSuccessCode implements BaseSuccessCode {

    BOARD_CREATED(HttpStatus.CREATED, "BOARD201_1", "게시글 생성에 성공했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
