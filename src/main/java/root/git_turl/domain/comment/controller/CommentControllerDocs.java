package root.git_turl.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import root.git_turl.domain.comment.dto.CommentReqDto;
import root.git_turl.domain.comment.dto.CommentResDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

public interface CommentControllerDocs {

    @Operation(
            summary = "댓글 작성",
            description = "게시글에 댓글을 작성합니다."
    )
    ApiResponse<CommentResDto.CommentCreateResultDto> createComment(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long boardId,
            @Valid @RequestBody CommentReqDto.CreateCommentReqDto request
    );

    @Operation(
            summary = "댓글 수정",
            description = "작성한 댓글을 수정합니다."
    )
    ApiResponse<CommentResDto.CommentUpdateResultDto> updateComment(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long commentId,
            @RequestBody CommentReqDto.UpdateCommentReqDto request
    );

    @Operation(
            summary = "댓글 삭제",
            description = "작성한 댓글을 삭제합니다."
    )
    ApiResponse<CommentResDto.CommentDeleteResultDto> deleteComment(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long commentId
    );

    @Operation(
            summary = "댓글 목록 조회",
            description = "게시글의 댓글 목록을 조회합니다."
    )
    ApiResponse<CommentResDto.CommentPreviewListDto> getCommentList(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") Integer page
    );
}