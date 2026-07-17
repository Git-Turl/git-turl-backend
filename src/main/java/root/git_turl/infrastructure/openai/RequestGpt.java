package root.git_turl.infrastructure.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.dto.gpt.GptMessage;
import root.git_turl.domain.report.dto.gpt.GptRequest;
import root.git_turl.domain.report.dto.gpt.GptResponse;
import root.git_turl.domain.report.exception.ReportException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestGpt {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String OPENAI_API = "https://api.openai.com/v1";

    @Value("${spring.ai.openai.api-key}")
    public String openAiApiKey;

    @Retryable(
            retryFor = HttpClientErrorException.class,
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 15000,
                    multiplier = 2
            )
    )
    public <T> T requestGpt(String prompt, Class<T> classType) {
        var retryContext = org.springframework.retry.support.RetrySynchronizationManager.getContext();
        int retryCount = (retryContext != null) ? retryContext.getRetryCount() : 0;

        log.warn("GPT 호출 재시도 {}회", retryCount);

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

        String json = response.getBody().getChoices().get(0).getMessage().getContent().trim();
        if (json.startsWith("```")) {
            json = json.replaceFirst("^```(?:json)?\\s*", "");
            json = json.replaceFirst("\\s*```$", "");
        }

        try {
            objectMapper.readTree(json);  // JSON 문법 검사
            return objectMapper.readValue(json, classType);
        } catch (JsonProcessingException e) {
            try {
                Files.writeString(
                        Path.of("failed-json-" + System.currentTimeMillis() + ".json"),
                        json
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            log.error("JSON parse failed", e);
            throw new RuntimeException("GPT 응답 파싱 실패: " + json, e);
        }
    }

    @Recover
    public <T> T recover(
            HttpClientErrorException.TooManyRequests e,
            String prompt,
            Class<T> classType) {

        log.error("GPT 호출이 모두 실패했습니다.", e);

        throw new ReportException(ReportErrorCode.OPENAI_RATE_LIMIT);
    }
}
