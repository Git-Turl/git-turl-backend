package root.git_turl.domain.report.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {

    GPT_RESPONSE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,
            "REPORT500_1",
            "요약본을 생성할 수 없습니다."),

    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND,
            "REPORT404_2",
            "존재하지 않는 요약본입니다."),

    REPO_NOT_FOUND(HttpStatus.NOT_FOUND,
            "REPORT500_1",
                    "존재하지 않는 레포지토리입니다."),

    NO_AUTH_REPORT(HttpStatus.FORBIDDEN,
            "REPORT403_1",
            "해당 요약본에 대한 접근 권한이 없습니다."),

    REPORT_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,
            "REPORT500_2",
            "정확도가 낮아 요약본을 생성에 실패했습니다."),

    OPENAI_RATE_LIMIT(
            HttpStatus.TOO_MANY_REQUESTS,
            "REPORT429_1",
            "현재 요청이 많아 일시적으로 리포트를 생성할 수 없습니다. 잠시 후 다시 시도해주세요."
    );

    private final HttpStatus status;
    private final String code;
    private final String message;
}
