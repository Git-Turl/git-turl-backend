package root.git_turl.domain.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.dto.QuestionReqDto;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

public interface QuestionControllerDocs {

    @Operation(
            summary = "분석본 토대로 질문 생성",
            description = "해당 분석본을 토대로 질문을 생성합니다."
    )
    public ApiResponse<Void> saveQuestion(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long reportId,
            @RequestBody @Valid QuestionReqDto.QuestionCount dto
    );
}
