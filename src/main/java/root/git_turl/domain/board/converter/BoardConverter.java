package root.git_turl.domain.board.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.BoardType;
import root.git_turl.domain.board.repository.BoardLikeRepository;
import root.git_turl.domain.board.repository.BoardPreviewProjection;
import root.git_turl.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BoardConverter {

    public Board toBoard(
            BoardReqDto.CreateBoardReqDto request,
            Member member,
            String imageUrl
    ) {
        return Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .boardType(request.getBoardType())

                // ===== 모집 공통 =====
                .recruitCount(request.getRecruitCount())
                .recruitDeadline(request.getRecruitDeadline())

                // ===== 스터디 =====
                .studyTag(request.getBoardType() == BoardType.STUDY ? request.getStudyTag() : null)
                .certificateType(
                        request.getBoardType() == BoardType.STUDY
                                ? request.getCertificateType()
                                : null
                )

                // ===== 프로젝트 =====
                .projectStatus(request.getBoardType() == BoardType.PROJECT ? request.getProjectStatus() : null)
                .recruitStacks(request.getBoardType() == BoardType.PROJECT ? request.getRecruitStacks() : List.of())
                .projectStacks(request.getBoardType() == BoardType.PROJECT ? request.getProjectStacks() : List.of())
                .platformTypes(request.getBoardType() == BoardType.PROJECT ? request.getPlatformTypes() : List.of())

                .imageUrl(imageUrl)
                .member(member)
                .build();
    }

    public BoardResDto.BoardDetailDto toBoardDetailDto(
            Board board,
            Long likeCount,
            Boolean isLiked
    ) {
        return BoardResDto.BoardDetailDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .boardType(board.getBoardType())

                // ===== 모집 공통 =====
                .recruitCount(board.getRecruitCount())
                .recruitDeadline(board.getRecruitDeadline())

                // ===== 스터디 =====
                .studyTag(board.getStudyTag())
                .certificateType(board.getCertificateType())

                // ===== 프로젝트 =====
                .projectStatus(board.getProjectStatus())
                .platformTypes(board.getPlatformTypes())
                .recruitStacks(board.getRecruitStacks())
                .projectStacks(board.getProjectStacks())

                .authorName(board.getMember().getNickname())
                .views(board.getViews())
                .likeCount(likeCount)
                .isLiked(isLiked)
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardResDto.BoardCreateResultDto toCreateResultDto(Board board) {
        return BoardResDto.BoardCreateResultDto.builder()
                .boardId(board.getId())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardResDto.BoardUpdateResultDto toUpdateResultDto(Board board) {
        return BoardResDto.BoardUpdateResultDto.builder()
                .boardId(board.getId())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public static BoardResDto.BoardDeleteResultDto toDeleteResultDto(Long boardId) {
        return BoardResDto.BoardDeleteResultDto.builder()
                .boardId(boardId)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static BoardResDto.BoardPreviewDto toBoardPreviewDto(
            Board board,
            Long likeCount
    ) {
        return BoardResDto.BoardPreviewDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .boardType(board.getBoardType())

                // ===== 모집 공통 =====
                .recruitCount(board.getRecruitCount())
                .recruitDeadline(board.getRecruitDeadline())

                // ===== 스터디 =====
                .studyTag(board.getStudyTag())
                .certificateType(board.getCertificateType())

                // ===== 프로젝트 =====
                .projectStatus(board.getProjectStatus())
                .platformTypes(board.getPlatformTypes())
                .recruitStacks(board.getRecruitStacks())
                .projectStacks(board.getProjectStacks())

                .writerName(board.getMember().getNickname())
                .likeCount(likeCount)
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardResDto.BoardPreviewListDto toBoardPreviewListDto(
            Page<BoardPreviewProjection> boardPage
    ) {
        List<BoardResDto.BoardPreviewDto> boardList = boardPage.stream()
                .map(p -> toBoardPreviewDto(
                        p.getBoard(),
                        p.getLikeCount()
                ))
                .toList();

        return BoardResDto.BoardPreviewListDto.builder()
                .boardList(boardList)
                .listSize(boardList.size())
                .totalPage(boardPage.getTotalPages())
                .totalElements(boardPage.getTotalElements())
                .isFirst(boardPage.isFirst())
                .isLast(boardPage.isLast())
                .build();
    }

    public BoardResDto.RecommendProjectDto toRecommendProjectDto(
            Board board,
            Long likeCount
    ) {
        return BoardResDto.RecommendProjectDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .recruitStacks(board.getRecruitStacks())
                .likeCount(likeCount)
                .views(board.getViews())
                .recruitCount(board.getRecruitCount())
                .build();
    }
}