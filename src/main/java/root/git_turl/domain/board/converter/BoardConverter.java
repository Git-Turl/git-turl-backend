package root.git_turl.domain.board.converter;

import org.hibernate.annotations.ConcreteProxy;
import org.springframework.stereotype.Component;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.member.entity.Member;

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

    public BoardResDto.BoardDetailDto toBoardDetailDto(Board board) {
        return BoardResDto.BoardDetailDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .boardType(board.getBoardType())
                .createdAt(board.getCreatedAt())
                .views(board.getViews())
                .build();
    }
}
