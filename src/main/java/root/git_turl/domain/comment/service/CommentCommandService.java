package root.git_turl.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.board.code.BoardErrorCode;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.exception.BoardException;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.comment.code.CommentErrorCode;
import root.git_turl.domain.comment.converter.CommentConverter;
import root.git_turl.domain.comment.dto.CommentReqDto;
import root.git_turl.domain.comment.dto.CommentResDto;
import root.git_turl.domain.comment.entity.Comment;
import root.git_turl.domain.comment.exception.CommentException;
import root.git_turl.domain.comment.repository.CommentRepository;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CommentCommandService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentConverter commentConverter;

    @Transactional
    public CommentResDto.CommentCreateResultDto createComment(
            Member currentMember,
            Long boardId,
            CommentReqDto.CreateCommentReqDto request
    ) {
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        Comment parent = null;

        if (request.getParentId() != null) {
            parent = commentRepository.findByIdAndDeletedAtIsNull(request.getParentId())
                    .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        }

        Comment comment = commentConverter.toComment(request, board, member, parent);
        commentRepository.save(comment);

        return CommentConverter.toCreateResultDto(comment);
    }

    @Transactional
    public CommentResDto.CommentUpdateResultDto updateComment(
            Member currentMember,
            Long commentId,
            CommentReqDto.UpdateCommentReqDto request
    ) {
        if (request.getContent() == null) {
            throw new CommentException(CommentErrorCode.NO_EDIT);
        }

        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        validateWriter(comment, currentMember);

        comment.update(
                request.getContent(),
                request.getIsSecret()
        );

        return CommentConverter.toUpdateResultDto(comment);
    }

    @Transactional
    public CommentResDto.CommentDeleteResultDto deleteComment(Member currentMember, Long commentId) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        validateWriter(comment, currentMember);

        comment.softDelete();

        return CommentConverter.toDeleteResultDto(commentId);
    }

    private void validateWriter(Comment comment, Member currentMember) {
        if (!comment.getMember().getId().equals(currentMember.getId())) {
            throw new CommentException(CommentErrorCode.NO_COMMENT_PERMISSION);
        }
    }
}
