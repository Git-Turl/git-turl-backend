package root.git_turl.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.enums.NotificationTag;
import root.git_turl.domain.notification.enums.NotificationTargetType;
import root.git_turl.global.entity.BaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "notification")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 발생시킨 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private Member actor;

    // 알림 받는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    // 이동할 게시글 ID
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    // 댓글 알림: 댓글 ID
    // 대댓글 알림: 부모 댓글 ID
    @Column(name = "target_object_id", nullable = false)
    private Long targetObjectId;

    // 현재는 게시글 화면으로 이동하므로 BOARD
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private NotificationTargetType targetType;

    // COMMENT, REPLY
    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false)
    private NotificationTag tag;

    // 알림 미리보기 내용
    @Column(name = "preview_content", nullable = false)
    private String previewContent;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    public void read() {
        this.isRead = true;
    }
}