package root.git_turl.domain.sample.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum SampleErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND,
            "SAMPLE404_1",
            "해당 샘플이 존재하지 않습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
