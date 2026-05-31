package root.git_turl.domain.notification.exception;

import root.git_turl.domain.notification.code.NotificationErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;

public class NotificationException extends GeneralException {

  public NotificationException(NotificationErrorCode code) {
    super(code);
  }
}