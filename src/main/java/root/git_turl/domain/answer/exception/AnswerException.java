package root.git_turl.domain.answer.exception;

import root.git_turl.global.apiPayload.code.BaseErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

public class AnswerException extends GeneralException {
    public AnswerException(BaseErrorCode code) {
        super(code);
    }
}
