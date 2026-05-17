package root.git_turl.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;

import root.git_turl.global.entity.BaseEntity;
import root.git_turl.domain.board.enums.BoardType;
import root.git_turl.domain.member.entity.Member;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="board")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String imageUrl;    // S3 업로드 후 URL 저장

    @Builder.Default
    @Column(nullable = false)
    private Integer views = 0;

    @Enumerated(EnumType.STRING)
    @Column(name="board_type", nullable = false)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void update(String title, String content, String imageUrl, BoardType boardType) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (boardType != null) this.boardType = boardType;
    }

    public void increaseViews() {
        this.views++;
    }
}
