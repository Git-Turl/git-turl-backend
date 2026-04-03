package root.git_turl.domain.sample.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@AllArgsConstructor
public enum SampleSuccessCode implements BaseSuccessCode {
    CREATE_OK(HttpStatus.OK,
            "SAMPLE200_1",
            "샘플 저장에 성공했습니다."),

    GET_OK(HttpStatus.OK,
            "SAMPLE200_2",
            "샘플 조회에 성공했습니다."),

    PATCH_OK(HttpStatus.OK,
            "SAMPLE200_3",
            "샘플 수정에 성공했습니다."),
    
    DELETE_OK(HttpStatus.OK,
            "SAMPLE200_4",
            "샘플 삭제에 성공했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
