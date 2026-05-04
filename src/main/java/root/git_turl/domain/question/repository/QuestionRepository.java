package root.git_turl.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.question.entity.Question;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
