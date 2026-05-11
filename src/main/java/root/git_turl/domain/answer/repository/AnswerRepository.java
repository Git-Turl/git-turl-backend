package root.git_turl.domain.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.question.entity.Question;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    long countByQuestion(Question question);
}
