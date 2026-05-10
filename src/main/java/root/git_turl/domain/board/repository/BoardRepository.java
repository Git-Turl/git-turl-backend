package root.git_turl.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
