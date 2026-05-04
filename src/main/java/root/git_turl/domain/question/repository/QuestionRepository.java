package root.git_turl.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
