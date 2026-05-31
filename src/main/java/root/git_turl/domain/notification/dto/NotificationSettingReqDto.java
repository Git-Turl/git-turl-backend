package root.git_turl.domain.notification.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class NotificationSettingReqDto {

    @Getter
    public static class UpdateNotificationSettingReqDto {

        @NotNull
        private Boolean commentNotificationEnabled;

        @NotNull
        private Boolean replyNotificationEnabled;
    }
}