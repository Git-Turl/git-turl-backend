package root.git_turl.domain.question.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.enums.Status;
import root.git_turl.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GenerationStatus status = GenerationStatus.PROCESSING;

    @Column(name = "time")
    private Integer time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(GenerationStatus status) {
        this.status = status;
    }

    public void updateTime(Integer time) { this.time = time; }
}
