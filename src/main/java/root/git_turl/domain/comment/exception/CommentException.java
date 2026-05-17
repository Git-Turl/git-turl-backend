package root.git_turl.domain.comment.exception;

import root.git_turl.global.apiPayload.code.BaseErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

public class CommentException extends GeneralException {
    public CommentException(BaseErrorCode code) {
        super(code);
    }
}
