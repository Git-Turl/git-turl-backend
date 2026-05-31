package root.git_turl.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.notification.entity.Notification;
import root.git_turl.domain.notification.enums.NotificationTag;
import root.git_turl.domain.notification.enums.NotificationTargetType;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResDto {

    private Long notificationId;

    private Long actorId;
    private String actorNickname;

    private Long receiverId;

    private Long boardId;
    private Long targetObjectId;

    private NotificationTargetType targetType;
    private NotificationTag tag;

    private String previewContent;
    private Boolean isRead;

    private LocalDateTime createdAt;

    public static NotificationResDto from(Notification notification) {
        return NotificationResDto.builder()
                .notificationId(notification.getId())
                .actorId(notification.getActor().getId())
                .receiverId(notification.getReceiver().getId())
                .boardId(notification.getBoardId())
                .targetObjectId(notification.getTargetObjectId())
                .targetType(notification.getTargetType())
                .tag(notification.getTag())
                .previewContent(notification.getPreviewContent())
                .isRead(notification.getIsRead())
                .build();
    }
}
