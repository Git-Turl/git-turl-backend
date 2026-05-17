package root.git_turl.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

public interface CommentLikeControllerDocs {

    @Operation(
            summary = "댓글 좋아요",
            description = "댓글에 좋아요를 추가합니다."
    )
    ApiResponse<Void> likeComment(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long commentId
    );

    @Operation(
            summary = "댓글 좋아요 취소",
            description = "댓글 좋아요를 취소합니다."
    )
    ApiResponse<Void> unlikeComment(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long commentId
    );
}