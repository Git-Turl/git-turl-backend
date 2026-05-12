package root.git_turl.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.comment.code.CommentSuccessCode;
import root.git_turl.domain.comment.dto.CommentReqDto;
import root.git_turl.domain.comment.dto.CommentResDto;
import root.git_turl.domain.comment.service.CommentCommandService;
import root.git_turl.domain.comment.service.CommentQueryService;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("/boards/{boardId}/comments")
    public ApiResponse<CommentResDto.CommentCreateResultDto> createComment(
            @CurrentUser Member member,
            @PathVariable Long boardId,
            @Valid @RequestBody CommentReqDto.CreateCommentReqDto request
    ) {
        return ApiResponse.onSuccess(
                CommentSuccessCode.COMMENT_CREATED,
                commentCommandService.createComment(member, boardId, request)
        );
    }

    @PatchMapping("/comments/{commentId}")
    public ApiResponse<CommentResDto.CommentUpdateResultDto> updateComment(
            @CurrentUser Member member,
            @PathVariable Long commentId,
            @RequestBody CommentReqDto.UpdateCommentReqDto request
    ) {
        return ApiResponse.onSuccess(
                CommentSuccessCode.COMMENT_UPDATED,
                commentCommandService.updateComment(member, commentId, request)
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<CommentResDto.CommentDeleteResultDto> deleteComment(
            @CurrentUser Member member,
            @PathVariable Long commentId
    ) {
        return ApiResponse.onSuccess(
                CommentSuccessCode.COMMENT_DELETED,
                commentCommandService.deleteComment(member, commentId)
        );
    }

    @GetMapping("/boards/{boardId}/comments")
    public ApiResponse<CommentResDto.CommentPreviewListDto> getCommentList(
            @CurrentUser Member member,
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        return ApiResponse.onSuccess(
                CommentSuccessCode.COMMENT_LIST_FOUND,
                commentQueryService.getCommentList(member, boardId, page)
        );
    }
}
