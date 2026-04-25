package root.git_turl.domain.report.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {

    GPT_RESPONSE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,
            "SUMMARY500_1",
            "요약본을 생성할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
