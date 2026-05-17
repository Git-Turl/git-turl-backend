package root.git_turl.domain.board.converter;

import org.springframework.stereotype.Component;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.repository.BoardLikeRepository;
import root.git_turl.domain.member.entity.Member;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BoardConverter {

    public Board toBoard(BoardReqDto.CreateBoardReqDto request, Member member, String imageUrl) {
        return Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .boardType(request.getBoardType())
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
                .writerName(board.getMember().getNickname())
                .likeCount(likeCount)
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardResDto.BoardPreviewListDto toBoardPreviewListDto(
            Page<Board> boardPage,
            BoardLikeRepository boardLikeRepository
    ) {

        List<BoardResDto.BoardPreviewDto> boardList = boardPage.stream()
                .map(board -> toBoardPreviewDto(
                        board,
                        boardLikeRepository.countByBoard(board)
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
}
