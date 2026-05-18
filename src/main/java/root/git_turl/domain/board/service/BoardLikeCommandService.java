package root.git_turl.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.board.code.BoardErrorCode;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.entity.BoardLike;
import root.git_turl.domain.board.exception.BoardException;
import root.git_turl.domain.board.repository.BoardLikeRepository;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class BoardLikeCommandService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void likeBoard(Member currentMember, Long boardId) {

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        boolean alreadyLiked =
                boardLikeRepository.existsByBoardAndMember(board, member);

        if (alreadyLiked) {
            return;
        }

        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .member(member)
                .build();

        boardLikeRepository.save(boardLike);
    }

    @Transactional
    public void unlikeBoard(Member currentMember, Long boardId) {

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        BoardLike boardLike =
                boardLikeRepository.findByBoardAndMember(board, member)
                        .orElse(null);

        if (boardLike == null) {
            return;
        }

        boardLikeRepository.delete(boardLike);
    }
}
