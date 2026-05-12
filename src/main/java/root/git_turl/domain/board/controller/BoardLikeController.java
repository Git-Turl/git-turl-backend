package root.git_turl.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.board.code.BoardSuccessCode;
import root.git_turl.domain.board.service.BoardLikeCommandService;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardLikeController {

    private final BoardLikeCommandService boardLikeCommandService;

    @PostMapping("/{boardId}/likes")
    public ApiResponse<Void> likeBoard(
            @CurrentUser Member member,
            @PathVariable Long boardId
    ) {
        boardLikeCommandService.likeBoard(member, boardId);

        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_LIKE_CREATED,
                null
        );
    }

    @DeleteMapping("/{boardId}/likes")
    public ApiResponse<Void> unlikeBoard(
            @CurrentUser Member member,
            @PathVariable Long boardId
    ) {
        boardLikeCommandService.unlikeBoard(member, boardId);

        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_LIKE_DELETED,
                null
        );
    }
}
