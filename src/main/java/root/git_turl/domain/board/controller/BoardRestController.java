package root.git_turl.domain.board.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.board.code.BoardSuccessCode;
import root.git_turl.domain.board.converter.BoardConverter;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.BoardType;
import root.git_turl.domain.board.service.BoardCommandService;
import root.git_turl.domain.board.service.BoardQueryService;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시판 API")
public class BoardRestController {

    private final BoardCommandService boardCommandService;
    private final BoardQueryService boardQueryService;
    private final BoardConverter boardConverter;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BoardResDto.BoardDetailDto> createBoard(
            @RequestPart("request") @Valid BoardReqDto.CreateBoardReqDto request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Member member = userDetails.getMember();

        Board board = boardCommandService.createBoard(request, member, image);
        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_CREATED,
                boardConverter.toBoardDetailDto(board)
        );
    }

    @PostMapping
    public ApiResponse<BoardResDto.BoardCreateResultDto> createBoard(
            @CurrentUser Member member,
            @Valid @RequestBody BoardReqDto.CreateBoardReqDto request
    ) {
        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_CREATED,
                boardCommandService.createBoard(member, request)
        );
    }

    @PatchMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BoardResDto.BoardUpdateResultDto> updateBoard(
            @CurrentUser Member member,
            @PathVariable Long boardId,
            @ModelAttribute BoardReqDto.UpdateDto request
    ) {
        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_UPDATED,
                boardCommandService.updateBoard(member, boardId, request)
        );
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<BoardResDto.BoardDeleteResultDto> deleteBoard(
            @CurrentUser Member member,
            @PathVariable Long boardId
    ) {
        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_DELETED,
                boardCommandService.deleteBoard(member, boardId)
        );
    }

    @GetMapping
    public ApiResponse<BoardResDto.BoardPreviewListDto> getBoardList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) BoardType boardType
    ) {
        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_LIST_FOUND,
                boardQueryService.getBoardList(page, boardType)
        );
    }
}
