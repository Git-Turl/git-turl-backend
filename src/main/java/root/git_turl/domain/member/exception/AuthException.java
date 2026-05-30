package root.git_turl.domain.member.exception;

import root.git_turl.global.apiPayload.code.BaseErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
