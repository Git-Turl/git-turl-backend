package root.git_turl.domain.answer.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum AnswerSuccessCode implements BaseSuccessCode {

    ANSWER_LIST_GET_OK(HttpStatus.OK,
            "ANSWER200_1",
            "답변과 피드백이 조회되었습니다."),

    ANSWER_POST_OK(HttpStatus.OK,
            "ANSWER200_2",
            "답변이 저장되었습니다"),

    FEEDBACK_POST_OK(HttpStatus.OK,
            "ANSWER200_3",
            "피드백이 생성되었습니다."),

    ANSWER_DELETE_OK(HttpStatus.OK,
            "ANSWER200_4",
            "답변이 삭제되었습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
