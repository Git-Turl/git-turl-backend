package root.git_turl.domain.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.dto.QuestionReqDto;
import root.git_turl.domain.question.dto.QuestionResDto;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;
import root.git_turl.global.util.Pagination;

import java.util.List;

public interface QuestionControllerDocs {

    @Operation(
            summary = "분석본 토대로 질문 생성",
            description = "해당 분석본을 토대로 질문을 생성합니다."
    )
    public ApiResponse<QuestionResDto.QuestionId> saveQuestion(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long reportId,
            @RequestBody @Valid QuestionReqDto.QuestionCount dto
    );

    @Operation(
            summary = "질문 목록 조회",
            description = "해당 분석본에 대한 질문 목록을 조회합니다."
    )
    public ApiResponse<ReportResDto.Pagination<QuestionResDto.QuestionInfo>> getQuestions(
            @PathVariable Long reportId,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String cursor
    );

    @Operation(
            summary = "질문 삭제",
            description = "해당 질문을 삭제합니다."
    )
    public ApiResponse<Void> deleteQuestion(
            @PathVariable Long questionId
    );
}
