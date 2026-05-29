package root.git_turl.domain.question.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorCode implements BaseErrorCode {

    FORBIDDEN(HttpStatus.FORBIDDEN,
            "QUESTION403_1",
            "해당 사용자가 접근할 수 없는 질문입니다."),


    NOT_FOUND(HttpStatus.NOT_FOUND,
            "QUESTION404_1",
            "존재하지 않는 질문입니다."),

    QUESTION_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,
            "QUESTION500_1",
            "품질 문제로 질문 생성에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
