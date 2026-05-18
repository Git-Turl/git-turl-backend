package root.git_turl.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.entity.BoardLike;
import root.git_turl.domain.member.entity.Member;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    Optional<BoardLike> findByBoardAndMember(
            Board board,
            Member member
    );

    Long countByBoard(Board board);

    Boolean existsByBoardAndMember(
            Board board,
            Member member
    );
}
