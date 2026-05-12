package root.git_turl.domain.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.question.entity.Question;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    long countByQuestion(Question question);

    @Query("""
        SELECT a
        FROM Answer a
        JOIN FETCH a.question
        WHERE a.id = :answerId
    """)
    Optional<Answer> findByIdWithQuestion(Long answerId);
}
