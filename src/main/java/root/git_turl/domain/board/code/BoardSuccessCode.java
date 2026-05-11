package root.git_turl.domain.board.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum BoardSuccessCode implements BaseSuccessCode {

    BOARD_CREATED(HttpStatus.CREATED,
            "BOARD201_1",
            "게시글 생성에 성공했습니다."),

    BOARD_UPDATED(HttpStatus.OK,
            "BOARD200_1",
            "게시글 수정에 성공했습니다."),

    BOARD_DELETED(HttpStatus.OK,
            "BOARD200_2",
            "게시글 삭제에 성공했습니다."),

    BOARD_LIST_FOUND(HttpStatus.OK,
            "BOARD200_3",
            "게시글 목록 조회에 성공했습니다."),

    BOARD_DETAIL_FOUND(
            HttpStatus.OK,
            "BOARD200_4",
            "게시글 상세 조회에 성공했습니다."
    );

    private final HttpStatus status;
    private final String code;
    private final String message;
}
