package root.git_turl.infrastructure.judge;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JudgeService {

    private final ChatClient judgeClient;

    public JudgeResult evaluate(String prompt) {
        return judgeClient.prompt()
                .user(prompt)
                .call()
                .entity(JudgeResult.class);
    }
}
