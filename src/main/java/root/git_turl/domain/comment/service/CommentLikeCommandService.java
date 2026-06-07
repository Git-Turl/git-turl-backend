package root.git_turl.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.comment.code.CommentErrorCode;
import root.git_turl.domain.comment.dto.CommentResDto;
import root.git_turl.domain.comment.entity.Comment;
import root.git_turl.domain.comment.entity.CommentLike;
import root.git_turl.domain.comment.exception.CommentException;
import root.git_turl.domain.comment.repository.CommentLikeRepository;
import root.git_turl.domain.comment.repository.CommentRepository;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CommentLikeCommandService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResDto.CommentLikeResultDto likeComment(
            Member currentMember,
            Long commentId
    ) {

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        boolean alreadyLiked =
                commentLikeRepository.existsByCommentAndMember(comment, member);

        if (!alreadyLiked) {
            CommentLike commentLike = CommentLike.builder()
                    .comment(comment)
                    .member(member)
                    .build();

            commentLikeRepository.save(commentLike);
        }

        return CommentResDto.CommentLikeResultDto.builder()
                .isLiked(true)
                .likeCount(commentLikeRepository.countByComment(comment))
                .build();
    }

    @Transactional
    public CommentResDto.CommentLikeResultDto unlikeComment(
            Member currentMember,
            Long commentId
    ) {

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        CommentLike commentLike =
                commentLikeRepository.findByCommentAndMember(comment, member)
                        .orElse(null);

        if (commentLike != null) {
            commentLikeRepository.delete(commentLike);
        }

        return CommentResDto.CommentLikeResultDto.builder()
                .isLiked(false)
                .likeCount(commentLikeRepository.countByComment(comment))
                .build();
    }
}