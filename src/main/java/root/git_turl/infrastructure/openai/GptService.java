package root.git_turl.infrastructure.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import root.git_turl.domain.answer.dto.AnswerResDto;
import root.git_turl.domain.answer.dto.Feedback;
import root.git_turl.domain.answer.dto.VoiceFeedback;
import root.git_turl.domain.question.dto.QuestionContent;
import root.git_turl.domain.report.dto.Problem;
import root.git_turl.domain.report.dto.ProblemList;
import root.git_turl.domain.report.dto.gpt.GptMessage;
import root.git_turl.domain.report.dto.gpt.GptRequest;
import root.git_turl.domain.report.dto.gpt.GptResponse;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GptService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String OPENAI_API = "https://api.openai.com/v1";

    @Value("${spring.ai.openai.api-key}")
    public String openAiApiKey;

    public ReportWrapper analyzeGit(String prompt) {
        return requestGpt(prompt, ReportWrapper.class);
    }

    public QuestionContent makeQuestions(String prompt) {
        return requestGpt(prompt, QuestionContent.class);
    }

    public Feedback makeFeedback(String prompt) {
        return requestGpt(prompt, Feedback.class);
    }

    public VoiceFeedback makeVoiceFeedback(String prompt) {
        return requestGpt(prompt, VoiceFeedback.class);
    }

    public ProblemList makeReportProblem(String prompt) {return requestGpt(prompt, ProblemList.class);}

    private <T> T requestGpt(String prompt, Class<T> classType) {
        GptRequest request = new GptRequest();
        request.setMessages(List.of(
                GptMessage.builder()
                        .role("user")
                        .content(prompt)
                        .build()
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<GptRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GptResponse> response =
                restTemplate.postForEntity(
                        OPENAI_API + "/chat/completions",
                        entity,
                        GptResponse.class
                );

        String json = response.getBody().getChoices().get(0).getMessage().getContent();
        try {
            return objectMapper.readValue(json, classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + json, e);
        }
    }
}
