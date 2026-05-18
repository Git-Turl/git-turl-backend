package root.git_turl.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.board.code.BoardErrorCode;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.exception.BoardException;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.comment.converter.CommentConverter;
import root.git_turl.domain.comment.dto.CommentResDto;
import root.git_turl.domain.comment.entity.Comment;
import root.git_turl.domain.comment.repository.CommentLikeRepository;
import root.git_turl.domain.comment.repository.CommentRepository;
import root.git_turl.domain.member.entity.Member;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional(readOnly = true)
    public CommentResDto.CommentPreviewListDto getCommentList(
            Member currentMember,
            Long boardId,
            Integer page
    ) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(
                page,
                10,
                Sort.by(Sort.Direction.ASC, "createdAt")
        );

        Page<Comment> commentPage =
                commentRepository.findCommentList(board, pageRequest);

        return CommentConverter.toCommentPreviewListDto(
                commentPage,
                currentMember,
                commentLikeRepository
        );
    }
}
