package root.git_turl.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.enums.*;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.util.List;

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

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(required = false)
            BoardType boardType,

            @RequestParam(required = false)
            StudyTag studyTag,

            @RequestParam(required = false)
            ProjectStatus projectStatus,

            @RequestParam(required = false)
            TechStack recruitStack,

            @RequestParam(required = false)
            TechStack projectStack,

            @RequestParam(required = false)
            PlatformType platformType,

            @RequestParam(defaultValue = "LATEST")
            BoardSortType sort
    );

    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글 상세 정보를 조회합니다."
    )
    ApiResponse<BoardResDto.BoardDetailDto> getBoardDetail(
            @CurrentUser @Parameter(hidden = true) Member member,
            @PathVariable Long boardId
    );

    @Operation(
            summary = "내가 작성한 게시글 조회",
            description = "로그인한 사용자가 작성한 게시글 목록을 조회합니다."
    )
    ApiResponse<BoardResDto.BoardPreviewListDto> getMyBoards(
            @CurrentUser @Parameter(hidden = true) Member member,

            @RequestParam(defaultValue = "0")
            Integer page
    );

    @Operation(
            summary = "특정 회원이 작성한 게시글 조회",
            description = "특정 회원이 작성한 게시글 목록을 조회합니다."
    )
    ApiResponse<BoardResDto.BoardPreviewListDto> getMemberBoards(
            @PathVariable Long memberId,

            @RequestParam(defaultValue = "0")
            Integer page
    );

    @Operation(
            summary = "추천 프로젝트 조회",
            description = "로그인한 사용자의 관심 기술스택과 프로젝트 구인스택을 기반으로 추천 프로젝트 게시글을 최대 3개 조회합니다."
    )
    ApiResponse<List<BoardResDto.RecommendProjectDto>> getRecommendProjects(
            @CurrentUser @Parameter(hidden = true) Member member
    );
}