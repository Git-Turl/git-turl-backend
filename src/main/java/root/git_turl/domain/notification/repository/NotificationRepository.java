package root.git_turl.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.dto.NotificationResDto;
import root.git_turl.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByReceiverOrderByCreatedAtDesc(Member receiver);

    List<Notification> findAllByReceiverAndIsReadFalseOrderByCreatedAtDesc(Member receiver);

    @Query("""
        SELECT new root.git_turl.domain.notification.dto.NotificationResDto(
            n.id,
            actor.id,
            actor.nickname,
            receiver.id,
            n.boardId,
            n.targetObjectId,
            n.targetType,
            n.tag,
            n.previewContent,
            n.isRead,
            n.createdAt
        )
        FROM Notification n
        JOIN n.actor actor
        JOIN n.receiver receiver
        WHERE receiver = :receiver
        ORDER BY n.createdAt DESC
    """)
    List<NotificationResDto> findAllDtoByReceiver(@Param("receiver") Member receiver);
}
