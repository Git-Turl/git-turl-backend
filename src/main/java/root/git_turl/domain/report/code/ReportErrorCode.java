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
            "해당 요약본에 대한 접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
