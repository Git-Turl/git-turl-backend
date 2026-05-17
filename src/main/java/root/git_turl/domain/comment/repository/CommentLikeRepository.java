package root.git_turl.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.comment.entity.Comment;
import root.git_turl.domain.comment.entity.CommentLike;
import root.git_turl.domain.member.entity.Member;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndMember(
            Comment comment,
            Member member
    );

    Boolean existsByCommentAndMember(
            Comment comment,
            Member member
    );

    Long countByComment(Comment comment);
}
