package root.git_turl.domain.answer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import root.git_turl.domain.answer.dto.AnswerReqDto;
import root.git_turl.domain.answer.dto.AnswerResDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.util.List;

public interface AnswerControllerDocs {

    @Operation(
            summary = "답변&피드백 조회",
            description = "답변과 피드백 목록을 조회합니다."
    )
    public ApiResponse<List<AnswerResDto.TextAnswer>> getAnswers(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long questionId
    );

    @Operation(
            summary = "답변 저장",
            description = "텍스트 형식으로 답변을 저장합니다."
    )
    public ApiResponse<Void> saveAnswer(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long questionId,
            @RequestBody AnswerReqDto.Answer dto
    );

    @Operation(
            summary = "피드백 생성",
            description = "해당 답변에 대한 피드백을 저장합니다."
    )
    public ApiResponse<Void> makeFeedback(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long answerId
    );

    @Operation(
            summary = "답변&피드백 삭제",
            description = "해당 답변과 피드백을 삭제합니다."
    )
    public ApiResponse<Void> deleteAnswer(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long answerId
    );
}
