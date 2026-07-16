package root.git_turl.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.comment.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndDeletedAtIsNull(Long commentId);

    boolean existsByParentAndDeletedAtIsNull(Comment parent);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        DELETE FROM Comment c
        WHERE c.board = :board
    """)
    void deleteAllByBoard(@Param("board") Board board);

    @Query("""
        select c
        from Comment c
        where c.board = :board
        order by
          coalesce(c.parent.id, c.id),
          c.depth asc,
          c.createdAt asc
    """)
    Page<Comment> findCommentList(
            @Param("board") Board board,
            Pageable pageable
    );

    @Query(
            value = """
    SELECT
        c.id AS commentId,
        b.id AS boardId,
        b.title AS boardTitle,
        b.boardType AS boardType,
        c.content AS content,
        c.isSecret AS isSecret,
        COUNT(DISTINCT cl.id) AS likeCount,
        c.createdAt AS createdAt
    FROM Comment c
    JOIN c.board b
    LEFT JOIN CommentLike cl ON cl.comment = c
    WHERE c.member.id = :memberId
      AND c.deletedAt IS NULL
    GROUP BY c.id, b.id, b.title, b.boardType, c.content, c.isSecret, c.createdAt
    ORDER BY c.createdAt DESC
""",
            countQuery = """
    SELECT COUNT(c)
    FROM Comment c
    WHERE c.member.id = :memberId
      AND c.deletedAt IS NULL
"""
    )
    Page<MyCommentProjection> findCommentsByMemberId(
            @Param("memberId") Long memberId,
            Pageable pageable
    );
}