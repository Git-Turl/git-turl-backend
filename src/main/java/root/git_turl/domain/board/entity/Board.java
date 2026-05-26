package root.git_turl.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;

import root.git_turl.domain.board.enums.*;
import root.git_turl.global.entity.BaseEntity;
import root.git_turl.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

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

    // ===== 스터디 게시판 =====

    @Enumerated(EnumType.STRING)
    @Column(name = "study_tag")
    private StudyTag studyTag;

    // ===== 프로젝트 게시판 =====

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @ElementCollection(targetClass = TechField.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "board_tech_fields",
            joinColumns = @JoinColumn(name = "board_id")
    )
    @Column(name = "tech_field")
    private List<TechField> techFields = new ArrayList<>();

    @ElementCollection(targetClass = PlatformType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "board_platform_types",
            joinColumns = @JoinColumn(name = "board_id")
    )
    @Column(name = "platform_type")
    private List<PlatformType> platformTypes = new ArrayList<>();


    public void update(
            String title,
            String content,
            String imageUrl,
            BoardType boardType,
            StudyTag studyTag,
            ProjectStatus projectStatus,
            List<TechField> techFields,
            List<PlatformType> platformTypes
    ) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (boardType != null) this.boardType = boardType;

        this.studyTag = studyTag;
        this.projectStatus = projectStatus;
        this.techFields = techFields;
        this.platformTypes = platformTypes;
    }

    public void increaseViews() {
        this.views++;
    }
}
