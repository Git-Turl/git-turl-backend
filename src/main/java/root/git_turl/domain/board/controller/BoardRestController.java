package root.git_turl.domain.board.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.board.code.BoardSuccessCode;
import root.git_turl.domain.board.enums.BoardType;
import root.git_turl.domain.board.service.BoardCommandService;
import root.git_turl.domain.board.service.BoardQueryService;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시판 API")
public class BoardRestController {

    private final BoardCommandService boardCommandService;
    private final BoardQueryService boardQueryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BoardResDto.BoardCreateResultDto> createBoard(
            @CurrentUser Member member,
            @RequestPart("request") @Valid BoardReqDto.CreateBoardReqDto request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {

        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_CREATED,
                boardCommandService.createBoard(member, request, image)
        );
    }

//    @PostMapping
//    public ApiResponse<BoardResDto.BoardCreateResultDto> createBoard(
//            @CurrentUser Member member,
//            @Valid @RequestBody BoardReqDto.CreateBoardReqDto request
//    ) {
//        return ApiResponse.onSuccess(
//                BoardSuccessCode.BOARD_CREATED,
//                boardCommandService.createBoard(member, request)
//        );
//    }

    @PatchMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BoardResDto.BoardUpdateResultDto> updateBoard(
            @CurrentUser Member member,
            @PathVariable Long boardId,
            @RequestPart("request") @Valid BoardReqDto.UpdateDto request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_UPDATED,
                boardCommandService.updateBoard(member, boardId, request, image)
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

    @GetMapping("/{boardId}")
    public ApiResponse<BoardResDto.BoardDetailDto> getBoardDetail(
            @CurrentUser Member member,
            @PathVariable Long boardId
    ) {
        return ApiResponse.onSuccess(
                BoardSuccessCode.BOARD_DETAIL_FOUND,
                boardQueryService.getBoardDetail(member, boardId)
        );
    }
}
