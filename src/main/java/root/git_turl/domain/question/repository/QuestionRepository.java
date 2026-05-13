package root.git_turl.domain.question.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.enums.GenerationStatus;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Slice<Question> findQuestionByReport_IdAndIdLessThanOrderByIdDesc(Long reportId, Long idCursor, PageRequest pageRequest);
    Slice<Question> findQuestionByReport_IdOrderByIdDesc(Long reportId, PageRequest pageRequest);
    long countByMemberAndStatus(Member member, GenerationStatus status);
}
