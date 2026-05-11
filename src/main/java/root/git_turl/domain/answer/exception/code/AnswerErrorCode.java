package root.git_turl.domain.answer.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum AnswerErrorCode implements BaseErrorCode {

    ANSWER_OVER_RAGE(HttpStatus.NOT_FOUND,
            "ANSWER400_1",
            "답변은 한 질문 당 3개까지 생성 가능합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
