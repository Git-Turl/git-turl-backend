package root.git_turl.domain.report.exception;

import root.git_turl.global.apiPayload.code.BaseErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

public class ReportException extends GeneralException {
    public ReportException(BaseErrorCode code) {
        super(code);
    }
}
