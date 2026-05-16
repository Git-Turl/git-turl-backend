package root.git_turl.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;

import root.git_turl.global.entity.BaseEntity;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_secret", nullable = false)
    @Builder.Default
    private Boolean isSecret = false;

    @Column(nullable = false)
    @Builder.Default
    private Integer depth = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void update(String content, Boolean isSecret) {
        if (content != null) {
            this.content = content;
        }

        if (isSecret != null) {
            this.isSecret = isSecret;
        }
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}