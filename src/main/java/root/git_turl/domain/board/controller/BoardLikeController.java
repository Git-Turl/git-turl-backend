package root.git_turl.domain.board.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.board.code.BoardSuccessCode;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.service.BoardLikeCommandService;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
@Tag(name = "Board Like", description = "게시판 좋아요 API")
public class BoardLikeController implements BoardLikeControllerDocs {

    private final BoardLikeCommandService boardLikeCommandService;

    @PostMapping("/{boardId}/likes")
    public ApiResponse<BoardResDto.BoardLikeResultDto> likeBoard(
            @CurrentUser Member member,
            @PathVariable Long boardId
    ) {
        BoardResDto.BoardLikeResultDto result =
                boardLikeCommandService.likeBoard(member, boardId);

        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_LIKE_CREATED,
                result
        );
    }

    @DeleteMapping("/{boardId}/likes")
    public ApiResponse<BoardResDto.BoardLikeResultDto> unlikeBoard(
            @CurrentUser Member member,
            @PathVariable Long boardId
    ) {
        BoardResDto.BoardLikeResultDto result =
                boardLikeCommandService.unlikeBoard(member, boardId);

        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_LIKE_DELETED,
                result
        );
    }
}
