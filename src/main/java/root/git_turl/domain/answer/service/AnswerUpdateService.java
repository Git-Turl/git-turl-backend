package root.git_turl.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.answer.repository.AnswerRepository;

@Service
@RequiredArgsConstructor
public class AnswerUpdateService {

    private final AnswerRepository answerRepository;

    @Transactional
    public void updateVoiceAnswer(
            Long answerId,
            String answerContent,
            String feedback,
            String summary,
            String keywords,
            Answer answer
    ) {


        answer.updateVoiceAnswer(
                answerContent,
                feedback,
                summary,
                keywords
        );
    }
}