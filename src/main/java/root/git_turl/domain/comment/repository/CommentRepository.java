package root.git_turl.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.comment.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndDeletedAtIsNull(Long commentId);

    Page<Comment> findAllByBoardAndDeletedAtIsNull(
            Board board,
            Pageable pageable
    );
}