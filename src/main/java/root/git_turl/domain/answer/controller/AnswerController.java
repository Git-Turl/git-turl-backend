package root.git_turl.domain.answer.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.answer.dto.AnswerReqDto;
import root.git_turl.domain.answer.exception.code.AnswerSuccessCode;
import root.git_turl.domain.answer.service.AnswerService;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Answer", description = "답변 관련 API")
public class AnswerController implements AnswerControllerDocs{

    private final AnswerService answerService;

    @PostMapping("/questions/{questionId}/answers")
    public ApiResponse<Void> saveAnswer(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long questionId,
            @RequestBody @Valid AnswerReqDto.Answer dto
    ) {
        answerService.saveAnswer(member, questionId, dto.getContent());
        return ApiResponse.onSuccess(AnswerSuccessCode.ANSWER_POST_OK, null);
    }
}
