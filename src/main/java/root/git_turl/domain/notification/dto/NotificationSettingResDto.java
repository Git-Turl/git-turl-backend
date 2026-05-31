package root.git_turl.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;

public class NotificationSettingResDto {

    @Getter
    @Builder
    public static class NotificationSettingDto {

        private Boolean commentNotificationEnabled;
        private Boolean replyNotificationEnabled;
    }
}