package root.git_turl.domain.answer.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.answer.dto.AnswerReqDto;
import root.git_turl.domain.answer.dto.AnswerResDto;
import root.git_turl.domain.answer.exception.code.AnswerSuccessCode;
import root.git_turl.domain.answer.service.AnswerService;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Answer", description = "답변 관련 API")
public class AnswerController implements AnswerControllerDocs{

    private final AnswerService answerService;

    @GetMapping("/questions/{questionId}/answers")
    public ApiResponse<List<AnswerResDto.TextAnswer>> getAnswers(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long questionId
    ) {
        List<AnswerResDto.TextAnswer> response = answerService.getAnswerList(member, questionId);
        return ApiResponse.onSuccess(AnswerSuccessCode.ANSWER_LIST_GET_OK, response);
    }

    @PostMapping("/questions/{questionId}/answers")
    public ApiResponse<Void> saveAnswer(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long questionId,
            @RequestBody @Valid AnswerReqDto.Answer dto
    ) {
        answerService.saveAnswer(member, questionId, dto.getContent());
        return ApiResponse.onSuccess(AnswerSuccessCode.ANSWER_POST_OK, null);
    }

    @PostMapping("/answers/{answerId}/feedbacks")
    public ApiResponse<Void> makeFeedback(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long answerId
    ) {
        answerService.makeFeedback(member, answerId);
        return ApiResponse.onSuccess(AnswerSuccessCode.FEEDBACK_POST_OK, null);
    }
}
