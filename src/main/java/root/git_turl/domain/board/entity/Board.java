package root.git_turl.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.BatchSize;
import root.git_turl.domain.board.enums.*;
import root.git_turl.global.entity.BaseEntity;
import root.git_turl.domain.member.entity.Member;

import java.time.LocalDate;
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

    // ===== 모집 공통 =====

    @Column(name = "recruit_count")
    private Integer recruitCount;

    @Column(name = "recruit_deadline")
    private LocalDate recruitDeadline;

    // ===== 스터디 게시판 =====

    @Enumerated(EnumType.STRING)
    @Column(name = "study_tag")
    private StudyTag studyTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "certificate_type")
    private CertificateType certificateType;

    // ===== 프로젝트 게시판 =====

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @BatchSize(size = 100)
    @ElementCollection(targetClass = TechStack.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "board_recruit_stacks",
            joinColumns = @JoinColumn(name = "board_id")
    )
    @Builder.Default
    @Column(name = "recruit_stack")
    private List<TechStack> recruitStacks = new ArrayList<>();

    @BatchSize(size = 100)
    @ElementCollection(targetClass = TechStack.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "board_project_stacks",
            joinColumns = @JoinColumn(name = "board_id")
    )
    @Builder.Default
    @Column(name = "project_stack")
    private List<TechStack> projectStacks = new ArrayList<>();

    @BatchSize(size = 100)
    @ElementCollection(targetClass = PlatformType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "board_platform_types",
            joinColumns = @JoinColumn(name = "board_id")
    )
    @Builder.Default
    @Column(name = "platform_type")
    private List<PlatformType> platformTypes = new ArrayList<>();


    public void update(
            String title,
            String content,
            String imageUrl,
            BoardType boardType,
            StudyTag studyTag,
            ProjectStatus projectStatus,
            List<PlatformType> platformTypes,
            Integer recruitCount,
            LocalDate recruitDeadline,
            CertificateType certificateType,
            List<TechStack> recruitStacks,
            List<TechStack> projectStacks
    ) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (boardType != null) this.boardType = boardType;

        this.studyTag = studyTag;
        this.projectStatus = projectStatus;
        this.platformTypes = platformTypes;

        this.recruitCount = recruitCount;
        this.recruitDeadline = recruitDeadline;
        this.certificateType = certificateType;
        this.recruitStacks = recruitStacks;
        this.projectStacks = projectStacks;
    }

    public void increaseViews() {
        this.views++;
    }
}
