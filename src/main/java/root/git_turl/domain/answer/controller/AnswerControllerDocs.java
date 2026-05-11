package root.git_turl.domain.answer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import root.git_turl.domain.answer.dto.AnswerReqDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

public interface AnswerControllerDocs {

    @Operation(
            summary = "답변 저장",
            description = "텍스트 형식으로 답변을 저장합니다."
    )
    public ApiResponse<Void> saveAnswer(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long questionId,
            @RequestBody AnswerReqDto.Answer dto
    );
}
