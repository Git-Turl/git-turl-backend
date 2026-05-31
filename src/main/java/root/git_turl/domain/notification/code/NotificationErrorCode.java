package root.git_turl.domain.notification.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements BaseErrorCode {

    NOTIFICATION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "NOTIFICATION404_1",
            "알림을 찾을 수 없습니다."
    ),

    NO_NOTIFICATION_PERMISSION(
            HttpStatus.FORBIDDEN,
            "NOTIFICATION403_1",
            "해당 알림에 접근할 권한이 없습니다."
    ),

    SSE_CONNECTION_FAILED(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "NOTIFICATION500_1",
            "SSE 연결에 실패했습니다."
    );

    private final HttpStatus status;
    private final String code;
    private final String message;
}