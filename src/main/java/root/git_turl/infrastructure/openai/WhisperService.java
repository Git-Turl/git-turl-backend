package root.git_turl.infrastructure.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import root.git_turl.domain.answer.dto.AnswerResDto;
import root.git_turl.domain.answer.entity.Answer;
import root.git_turl.domain.report.enums.GenerationStatus;

import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class WhisperService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String OPENAI_API = "https://api.openai.com/v1";

    @Value("${openai.api-key}")
    public String openAiApiKey;

    public String transcribe(String url, Answer answer) {
        try {

            InputStream inputStream = new URL(url).openStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(openAiApiKey);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("model", "whisper-1");

            body.add("file", new MultipartInputStreamFileResource(
                    inputStream,
                    "audio.webm"
            ));

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<AnswerResDto.TranscriptionResponse> response =
                    restTemplate.exchange(
                            OPENAI_API + "/audio/transcriptions",
                            HttpMethod.POST,
                            requestEntity,
                            AnswerResDto.TranscriptionResponse.class
                    );

            if (response.getBody() == null) {
                answer.updateGenerationStatus(GenerationStatus.FAIL);
                throw new RuntimeException("STT 응답이 비어있음");
            }

            return response.getBody().getText();

        } catch (Exception e) {
            answer.updateGenerationStatus(GenerationStatus.FAIL);
            throw new RuntimeException("음성 파일 처리 실패", e);
        }
    }
}
