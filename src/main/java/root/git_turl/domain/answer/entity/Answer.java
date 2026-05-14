package root.git_turl.domain.answer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import root.git_turl.domain.answer.enums.AnswerType;
import root.git_turl.domain.answer.enums.Status;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.global.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    @Column(name = "content")
    private String content;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "voice_file")
    private String voiceFile;

    @Column(name = "answer_summary")
    private String answerSummary;

    @Column(name = "keyword")
    private List<String> keyword;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;


    public void updateFeedback(String feedback) {
        this.feedback = feedback;
    }
}
