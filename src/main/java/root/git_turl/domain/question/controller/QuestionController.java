package root.git_turl.domain.question.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.dto.QuestionReqDto;
import root.git_turl.domain.question.dto.QuestionResDto;
import root.git_turl.domain.question.exception.code.QuestionSuccessCode;
import root.git_turl.domain.question.service.QuestionService;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;
import root.git_turl.global.util.Pagination;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Question", description = "질문 관련 API")
public class QuestionController implements QuestionControllerDocs{

    private final QuestionService questionService;

    @PostMapping("/reports/{reportId}/questions")
    public ApiResponse<QuestionResDto.QuestionId> saveQuestion(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long reportId,
            @RequestBody @Valid QuestionReqDto.QuestionCount dto
    ) {
        QuestionResDto.QuestionId response = questionService.saveQuestion(member, reportId, dto);
        return ApiResponse.onSuccess(QuestionSuccessCode.QUESTION_POST_OK, response);
    }

    @GetMapping("/reports/{reportId}/questions")
    public ApiResponse<ReportResDto.Pagination<QuestionResDto.QuestionInfo>> getQuestions(
            @PathVariable Long reportId,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String cursor
    ) {
        ReportResDto.Pagination<QuestionResDto.QuestionInfo> response = questionService.getQuestionList(reportId, pageSize, cursor);
        return ApiResponse.onSuccess(QuestionSuccessCode.QUESTION_LIST_GET_OK, response);
    }

    @DeleteMapping("/questions/{questionId}")
    public ApiResponse<Void> deleteQuestion(
            @PathVariable Long questionId
    ) {
        questionService.deleteQuestion(questionId);
        return ApiResponse.onSuccess(QuestionSuccessCode.QUESTION_DELETE_OK, null);
    }
}
