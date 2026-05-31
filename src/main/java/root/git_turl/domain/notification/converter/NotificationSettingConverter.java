package root.git_turl.domain.notification.converter;

import org.springframework.stereotype.Component;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.dto.NotificationSettingResDto;
import root.git_turl.domain.notification.entity.NotificationSetting;

@Component
public class NotificationSettingConverter {

    public NotificationSetting toDefaultSetting(Member member) {
        return NotificationSetting.builder()
                .member(member)
                .commentNotificationEnabled(true)
                .replyNotificationEnabled(true)
                .build();
    }

    public NotificationSettingResDto.NotificationSettingDto toDto(NotificationSetting setting) {
        return NotificationSettingResDto.NotificationSettingDto.builder()
                .commentNotificationEnabled(setting.getCommentNotificationEnabled())
                .replyNotificationEnabled(setting.getReplyNotificationEnabled())
                .build();
    }
}