package root.git_turl.domain.question.exception;

import root.git_turl.global.apiPayload.code.BaseErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

public class QuestionException extends GeneralException {
    public QuestionException(BaseErrorCode code) {
        super(code);
    }
}
