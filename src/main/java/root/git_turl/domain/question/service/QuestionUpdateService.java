package root.git_turl.domain.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.question.repository.QuestionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionUpdateService {

    private final QuestionRepository questionRepository;

    @Transactional
    public void updateQuestions(List<Question> questions) {
        questionRepository.saveAll(questions);
    }
}
