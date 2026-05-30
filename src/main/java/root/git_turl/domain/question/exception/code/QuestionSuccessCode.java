package root.git_turl.domain.question.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum QuestionSuccessCode implements BaseSuccessCode {

    QUESTION_LIST_GET_OK(HttpStatus.OK,
            "QUESTION200_1",
            "질문 목록이 조회되었습니다."),

    QUESTION_POST_OK(HttpStatus.OK,
            "QUESTION200_2",
            "질문을 생성하였습니다."),

    QUESTION_DELETE_OK(HttpStatus.OK,
            "QUESTION200_3",
            "질문이 삭제되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
