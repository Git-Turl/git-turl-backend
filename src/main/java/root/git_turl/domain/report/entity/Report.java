package root.git_turl.domain.report.entity;

import jakarta.persistence.*;
import lombok.*;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.enums.Status;
import root.git_turl.global.entity.BaseEntity;

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

    public void updateGenerationStatus(GenerationStatus status) {
        this.generationStatus = status;
    }

    public void updateContent(String contentJson) {
        this.contentJson = contentJson;
    }

    public void updateStatus(Status status) { this.status = status; }
}
