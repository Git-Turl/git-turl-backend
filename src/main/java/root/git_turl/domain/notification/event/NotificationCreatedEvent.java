package root.git_turl.domain.notification.event;
import root.git_turl.domain.notification.dto.NotificationResDto;

public record NotificationCreatedEvent(
        Long receiverId,
        NotificationResDto notification
) {
}