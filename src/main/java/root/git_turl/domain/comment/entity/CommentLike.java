package root.git_turl.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "comment_like",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"comment_id", "member_id"}
                )
        }
)
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
