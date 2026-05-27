package root.git_turl.domain.board.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.board.enums.*;


public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByBoardType(BoardType boardType, Pageable pageable);

    // 최신순
    @Query("""
        SELECT DISTINCT b
        FROM Board b
        LEFT JOIN b.techFields tf
        LEFT JOIN b.platformTypes pt
        WHERE (:boardType IS NULL OR b.boardType = :boardType)
          AND (:studyTag IS NULL OR b.studyTag = :studyTag)
          AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
          AND (:techField IS NULL OR tf = :techField)
          AND (:platformType IS NULL OR pt = :platformType)
        ORDER BY b.createdAt DESC
        """)
    Page<Board> findBoardListWithFiltersOrderByLatest(
            BoardType boardType,
            StudyTag studyTag,
            ProjectStatus projectStatus,
            TechField techField,
            PlatformType platformType,
            Pageable pageable
    );

    // 좋아요순
    @Query("""
        SELECT b
        FROM Board b
        LEFT JOIN b.techFields tf
        LEFT JOIN b.platformTypes pt
        LEFT JOIN BoardLike bl ON bl.board = b
        WHERE (:boardType IS NULL OR b.boardType = :boardType)
          AND (:studyTag IS NULL OR b.studyTag = :studyTag)
          AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
          AND (:techField IS NULL OR tf = :techField)
          AND (:platformType IS NULL OR pt = :platformType)
        GROUP BY b
        ORDER BY COUNT(bl) DESC, b.createdAt DESC
        """)
    Page<Board> findBoardListWithFiltersOrderByLike(
            BoardType boardType,
            StudyTag studyTag,
            ProjectStatus projectStatus,
            TechField techField,
            PlatformType platformType,
            Pageable pageable
    );


    //Optional<Board> findById(Long id);
}
