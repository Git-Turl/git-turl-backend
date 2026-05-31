package root.git_turl.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.entity.BaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "notification_setting")
public class NotificationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 설정 주인
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    // 게시글 댓글 알림
    @Column(name = "comment_notification_enabled", nullable = false)
    private Boolean commentNotificationEnabled;

    // 대댓글 알림
    @Column(name = "reply_notification_enabled", nullable = false)
    private Boolean replyNotificationEnabled;

    public void update(Boolean commentNotificationEnabled, Boolean replyNotificationEnabled) {
        this.commentNotificationEnabled = commentNotificationEnabled;
        this.replyNotificationEnabled = replyNotificationEnabled;
    }
}