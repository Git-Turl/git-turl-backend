package root.git_turl.domain.question.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Slice<Question> findQuestionByReport_IdAndIdLessThanOrderByIdDesc(Long reportId, Long idCursor, PageRequest pageRequest);
    Slice<Question> findQuestionByReport_IdOrderByIdDesc(Long reportId, PageRequest pageRequest);
    long countByMemberAndStatus(Member member, GenerationStatus status);
    long countByReportAndStatus(Report report, GenerationStatus status);

    @Query("""
            SELECT q
            FROM Question q
            JOIN FETCH q.answerList
            WHERE q.id = :questionId
    """)
    Optional<Question> findQuestionWithAnswer(Long questionId);
}
