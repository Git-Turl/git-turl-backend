package root.git_turl.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.entity.NotificationSetting;

import java.util.Optional;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {

    Optional<NotificationSetting> findByMember(Member member);

    Optional<NotificationSetting> findByMemberId(Long memberId);
}