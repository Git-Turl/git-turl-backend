package root.git_turl.domain.board.exception;

import root.git_turl.global.apiPayload.code.BaseErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

public class BoardException extends GeneralException {
    public BoardException(BaseErrorCode code) {
        super(code);
    }
}
