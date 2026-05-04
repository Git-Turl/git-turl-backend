package root.git_turl.domain.question.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.dto.QuestionReqDto;
import root.git_turl.domain.question.exception.code.QuestionSuccessCode;
import root.git_turl.domain.question.service.QuestionService;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Question", description = "질문 관련 API")
public class QuestionController implements QuestionControllerDocs{

    private final QuestionService questionService;

    @PostMapping("/reports/{reportId}/questions")
    public ApiResponse<Void> saveQuestion(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long reportId,
            @RequestBody @Valid QuestionReqDto.QuestionCount dto
    ) {
        questionService.saveQuestion(member, reportId, dto);
        return ApiResponse.onSuccess(QuestionSuccessCode.QUESTION_POST_OK, null);
    }
}
