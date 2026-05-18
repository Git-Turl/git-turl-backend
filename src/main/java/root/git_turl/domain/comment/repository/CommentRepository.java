package root.git_turl.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.comment.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndDeletedAtIsNull(Long commentId);

    @Query("""
        select c
        from Comment c
        where c.board = :board
          and c.deletedAt is null
        order by
          coalesce(c.parent.id, c.id),
          c.depth asc,
          c.createdAt asc
    """)
    Page<Comment> findCommentList(
            @Param("board") Board board,
            Pageable pageable
    );
}