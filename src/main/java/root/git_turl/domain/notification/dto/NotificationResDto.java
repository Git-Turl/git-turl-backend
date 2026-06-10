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

    public NotificationResDto(
            Long notificationId,
            Long actorId,
            String actorNickname,
            Long receiverId,
            Long boardId,
            Long targetObjectId,
            NotificationTargetType targetType,
            NotificationTag tag,
            String previewContent,
            Boolean isRead,
            LocalDateTime createdAt
    ) {
        this.notificationId = notificationId;
        this.actorId = actorId;
        this.actorNickname = actorNickname;
        this.receiverId = receiverId;
        this.boardId = boardId;
        this.targetObjectId = targetObjectId;
        this.targetType = targetType;
        this.tag = tag;
        this.previewContent = previewContent;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

}
