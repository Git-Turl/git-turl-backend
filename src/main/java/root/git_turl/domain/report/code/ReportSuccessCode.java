package root.git_turl.domain.report.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum ReportSuccessCode implements BaseSuccessCode {
    REPORT_LIST_GET_OK(HttpStatus.OK,
            "REPORT200_1",
            "요약본 목록이 조회되었습니다."),

    REPO_LIST_GET_OK(HttpStatus.OK,
            "REPORT200_2",
            "레포지토리 목록이 조회되었습니다."),

    REPORT_POST_OK(HttpStatus.OK,
            "REPORT200_3",
            "레포지토리 요약본이 생성되었습니다."),

    REPORT_GET_OK(HttpStatus.OK,
            "REPORT200_4",
            "레포지토리 요약본이 조회되었습니다."),

    REPORT_STATUS_PATCH_OK(HttpStatus.OK,
    "REPORT200_5",
            "요약본 공개여부가 변경되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
