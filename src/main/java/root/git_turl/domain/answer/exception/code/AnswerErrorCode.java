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
            "답변은 한 질문 당 3개까지 생성 가능합니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND,
            "ANSWER404_1",
            "해당 답변이 존재하지 않습니다."),

    FEEDBACK_ALREADY_EXISTS(HttpStatus.CONFLICT,
            "ANSWER409_1",
            "이미 피드백이 존재합니다."),

    VOICE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,
            "VOICE500_1",
            "영상 업로드에 실패했습니다."),

    FEEDBACK_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,
            "FEEDBACK500_1",
            "품질 문제로 피드백 생성에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
