package root.git_turl.domain.notification.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum NotificationSuccessCode implements BaseSuccessCode {

    NOTIFICATION_LIST_FOUND(
            HttpStatus.OK,
            "NOTIFICATION200_1",
            "알림 목록 조회에 성공했습니다."
    ),

    NOTIFICATION_READ(
            HttpStatus.OK,
            "NOTIFICATION200_2",
            "알림 읽음 처리에 성공했습니다."
    ),

    NOTIFICATION_SUBSCRIBE(
            HttpStatus.OK,
            "NOTIFICATION200_3",
            "알림 구독에 성공했습니다."
    ),

    NOTIFICATION_SETTING_FOUND(
            HttpStatus.OK,
            "NOTIFICATION200_4",
            "알림 설정 조회에 성공했습니다."
    ),

    NOTIFICATION_SETTING_UPDATED(
            HttpStatus.OK,
            "NOTIFICATION200_5",
            "알림 설정 변경에 성공했습니다."
    );;

    private final HttpStatus status;
    private final String code;
    private final String message;
}