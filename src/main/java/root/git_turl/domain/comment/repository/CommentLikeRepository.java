package root.git_turl.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.git_turl.domain.board.entity.Board;
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

    void deleteAllByComment(Comment comment);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        DELETE FROM CommentLike cl
        WHERE cl.comment.board = :board
    """)
    void deleteAllByCommentBoard(@Param("board") Board board);
}