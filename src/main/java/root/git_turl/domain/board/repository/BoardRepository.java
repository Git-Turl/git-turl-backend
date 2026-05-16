package root.git_turl.domain.board.repository;

import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByBoardType(BoardType boardType, Pageable pageable);

    //Optional<Board> findById(Long id);
}
