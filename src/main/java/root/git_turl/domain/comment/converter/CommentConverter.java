package root.git_turl.domain.comment.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.comment.dto.CommentReqDto;
import root.git_turl.domain.comment.dto.CommentResDto;
import root.git_turl.domain.comment.entity.Comment;
import root.git_turl.domain.comment.repository.CommentLikeRepository;
import root.git_turl.domain.comment.repository.MyCommentProjection;
import root.git_turl.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CommentConverter {

    public Comment toComment(
            CommentReqDto.CreateCommentReqDto request,
            Board board,
            Member member,
            Comment parent
    ) {
        return Comment.builder()
                .content(request.getContent())
                .board(board)
                .member(member)
                .parent(parent)
                .isSecret(
                        request.getIsSecret() != null
                                ? request.getIsSecret()
                                : false
                )
                .depth(parent == null ? 0 : parent.getDepth() + 1)
                .build();
    }

    public static CommentResDto.CommentCreateResultDto toCreateResultDto(Comment comment) {
        return CommentResDto.CommentCreateResultDto.builder()
                .commentId(comment.getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResDto.CommentUpdateResultDto toUpdateResultDto(Comment comment) {
        return CommentResDto.CommentUpdateResultDto.builder()
                .commentId(comment.getId())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static CommentResDto.CommentDeleteResultDto toDeleteResultDto(Long commentId) {
        return CommentResDto.CommentDeleteResultDto.builder()
                .commentId(commentId)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static CommentResDto.CommentPreviewDto toCommentPreviewDto(
            Comment comment,
            Member currentMember,
            Long likeCount,
            Boolean isLiked
    ) {

        boolean isDeleted = comment.getDeletedAt() != null;

        boolean canReadSecretComment =
                !comment.getIsSecret()
                        || comment.getMember().getId().equals(currentMember.getId())
                        || comment.getBoard().getMember().getId().equals(currentMember.getId());

        return CommentResDto.CommentPreviewDto.builder()
                .commentId(comment.getId())
                .parentId(
                        comment.getParent() == null
                                ? null
                                : comment.getParent().getId()
                )
                .depth(comment.getDepth())
                .isSecret(comment.getIsSecret())
                .isDeleted(isDeleted)
                .content(
                        isDeleted
                                ? "삭제된 댓글입니다."
                                : canReadSecretComment
                                    ? comment.getContent()
                                    : "비밀 댓글입니다."
                )
                .writerId(comment.getMember().getId())
                .writerName(comment.getMember().getNickname())
                .profileImage(comment.getMember().getProfileImage())
                .likeCount(likeCount)
                .isLiked(isLiked)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResDto.CommentPreviewListDto toCommentPreviewListDto(
            Page<Comment> commentPage,
            Member currentMember,
            CommentLikeRepository commentLikeRepository
    ) {

        List<CommentResDto.CommentPreviewDto> commentList = commentPage.stream()
                .map(comment -> toCommentPreviewDto(
                        comment,
                        currentMember,
                        commentLikeRepository.countByComment(comment),
                        commentLikeRepository.existsByCommentAndMember(
                                comment,
                                currentMember
                        )
                ))
                .toList();

        return CommentResDto.CommentPreviewListDto.builder()
                .commentList(commentList)
                .listSize(commentList.size())
                .totalPage(commentPage.getTotalPages())
                .totalElements(commentPage.getTotalElements())
                .isFirst(commentPage.isFirst())
                .isLast(commentPage.isLast())
                .build();
    }

    public static CommentResDto.MyCommentPreviewDto toMyCommentPreviewDto(
            MyCommentProjection projection
    ) {
        return CommentResDto.MyCommentPreviewDto.builder()
                .commentId(projection.getCommentId())
                .boardId(projection.getBoardId())
                .boardTitle(projection.getBoardTitle())
                .boardType(projection.getBoardType())
                .content(projection.getContent())
                .likeCount(projection.getLikeCount())
                .createdAt(projection.getCreatedAt())
                .build();
    }

    public static CommentResDto.MyCommentPreviewListDto toMyCommentPreviewListDto(
            Page<MyCommentProjection> commentPage
    ) {
        List<CommentResDto.MyCommentPreviewDto> commentList = commentPage.stream()
                .map(CommentConverter::toMyCommentPreviewDto)
                .toList();

        return CommentResDto.MyCommentPreviewListDto.builder()
                .commentList(commentList)
                .listSize(commentList.size())
                .totalPage(commentPage.getTotalPages())
                .totalElements(commentPage.getTotalElements())
                .isFirst(commentPage.isFirst())
                .isLast(commentPage.isLast())
                .build();
    }
}
