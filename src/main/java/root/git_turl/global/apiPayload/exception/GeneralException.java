package root.git_turl.global.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{
    private final BaseErrorCode code;
}
