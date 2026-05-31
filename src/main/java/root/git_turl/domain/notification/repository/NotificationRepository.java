package root.git_turl.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByReceiverOrderByCreatedAtDesc(Member receiver);

    List<Notification> findAllByReceiverAndIsReadFalseOrderByCreatedAtDesc(Member receiver);
}
