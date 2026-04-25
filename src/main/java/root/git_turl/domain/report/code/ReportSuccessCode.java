package root.git_turl.domain.report.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum ReportSuccessCode implements BaseSuccessCode {

    REPO_LIST_GET_OK(HttpStatus.OK,
            "REPORT200_2",
            "레포지토리 목록이 조회되었습니다."),

    REPORT_POST_OK(HttpStatus.OK,
            "REPORT200_3",
            "레포지토리 요약본이 생성되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
