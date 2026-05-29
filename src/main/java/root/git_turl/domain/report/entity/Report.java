package root.git_turl.domain.report.entity;

import jakarta.persistence.*;
import lombok.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.enums.Status;
import root.git_turl.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report")
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "repo_name")
    private String repoName;

    @Column(name = "github_id")
    private String githubId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private GenerationStatus generationStatus;

    @Column(columnDefinition = "TEXT")
    private String contentJson;

    @Column(name = "description")
    private String description;

    @Column(name = "features")
    private String features;

    @Column(name = "improvements")
    private String improvements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "report")
    private List<Question> questions = new ArrayList<>();

    public void updateTitle(String title) { this.title = title; }

    public void updateGenerationStatus(GenerationStatus status) {
        this.generationStatus = status;
    }

    public void updateContent(String contentJson) {
        this.contentJson = contentJson;
    }

    public void updateStatus(Status status) { this.status = status; }

    public void updateDescription(String description) { this.description = description; }
}
