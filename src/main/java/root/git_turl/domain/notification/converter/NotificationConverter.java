package root.git_turl.domain.notification.converter;

import org.springframework.stereotype.Component;
import root.git_turl.domain.notification.dto.NotificationResDto;
import root.git_turl.domain.notification.entity.Notification;

import java.util.List;

@Component
public class NotificationConverter {

    public NotificationResDto toNotificationResDto(Notification notification) {
        return NotificationResDto.builder()
                .notificationId(notification.getId())
                .actorId(notification.getActor().getId())
                .actorNickname(notification.getActor().getNickname())
                .receiverId(notification.getReceiver().getId())
                .boardId(notification.getBoardId())
                .targetObjectId(notification.getTargetObjectId())
                .targetType(notification.getTargetType())
                .tag(notification.getTag())
                .previewContent(notification.getPreviewContent())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public List<NotificationResDto> toNotificationResDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toNotificationResDto)
                .toList();
    }
}
