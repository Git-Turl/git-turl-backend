package root.git_turl.domain.sample.exception;

import root.git_turl.global.apiPayload.code.BaseErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

public class SampleException extends GeneralException {
    public SampleException(BaseErrorCode code) {
        super(code);
    }
}
