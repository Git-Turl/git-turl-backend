package root.git_turl.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.enums.BoardType;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

public interface BoardControllerDocs {

    @Operation(
            summary = "게시글 작성",
            description = "게시글을 작성합니다."
    )
    ApiResponse<BoardResDto.BoardCreateResultDto> createBoard(
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestPart("request") @Valid BoardReqDto.CreateBoardReqDto request,
            @RequestPart(value = "image", required = false) MultipartFile image
    );

    @Operation(
            summary = "게시글 수정",
            description = "작성한 게시글을 수정합니다."
    )
    ApiResponse<BoardResDto.BoardUpdateResultDto> updateBoard(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long boardId,
            @RequestPart("request") @Valid BoardReqDto.UpdateDto request,
            @RequestPart(value = "image", required = false) MultipartFile image
    );

    @Operation(
            summary = "게시글 삭제",
            description = "작성한 게시글을 삭제합니다."
    )
    ApiResponse<BoardResDto.BoardDeleteResultDto> deleteBoard(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long boardId
    );

    @Operation(
            summary = "게시글 목록 조회",
            description = "게시글 목록을 조회합니다."
    )
    ApiResponse<BoardResDto.BoardPreviewListDto> getBoardList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) BoardType boardType
    );

    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글 상세 정보를 조회합니다."
    )
    ApiResponse<BoardResDto.BoardDetailDto> getBoardDetail(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long boardId
    );
}