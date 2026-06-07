package root.git_turl.domain.comment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.comment.code.CommentSuccessCode;
import root.git_turl.domain.comment.dto.CommentResDto;
import root.git_turl.domain.comment.service.CommentLikeCommandService;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Comment Like", description = "댓글 좋아요 API")
public class CommentLikeController implements CommentLikeControllerDocs {

    private final CommentLikeCommandService commentLikeCommandService;


    @PostMapping("/{commentId}/likes")
    public ApiResponse<CommentResDto.CommentLikeResultDto> likeComment(
            @CurrentUser Member member,
            @PathVariable Long commentId
    ) {

        CommentResDto.CommentLikeResultDto result =
                commentLikeCommandService.likeComment(member, commentId);

        return ApiResponse.onSuccess(
                CommentSuccessCode.COMMENT_LIKE_CREATED,
                result
        );
    }

    @DeleteMapping("/{commentId}/likes")
    public ApiResponse<CommentResDto.CommentLikeResultDto> unlikeComment(
            @CurrentUser Member member,
            @PathVariable Long commentId
    ) {

        CommentResDto.CommentLikeResultDto result =
                commentLikeCommandService.unlikeComment(member, commentId);

        return ApiResponse.onSuccess(
                CommentSuccessCode.COMMENT_LIKE_DELETED,
                result
        );
    }
}
