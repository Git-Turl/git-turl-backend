package root.git_turl.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.*;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByBoardType(BoardType boardType, Pageable pageable);

    @Query("""
        SELECT DISTINCT b
        FROM Board b
        LEFT JOIN b.recruitStacks rs
        LEFT JOIN b.projectStacks ps
        LEFT JOIN b.platformTypes pt
        LEFT JOIN BoardLike bl ON bl.board = b
        WHERE (:boardType IS NULL OR b.boardType = :boardType)
          AND (:studyTag IS NULL OR b.studyTag = :studyTag)
          AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
          AND (:recruitStack IS NULL OR rs = :recruitStack)
          AND (:projectStack IS NULL OR ps = :projectStack)
          AND (:platformType IS NULL OR pt = :platformType)
        GROUP BY b
        ORDER BY
          CASE WHEN :sort = root.git_turl.domain.board.enums.BoardSortType.LIKE THEN COUNT(bl) END DESC,
          b.createdAt DESC
        """)
    Page<Board> findBoardListWithFilters(
            @Param("boardType") BoardType boardType,
            @Param("studyTag") StudyTag studyTag,
            @Param("projectStatus") ProjectStatus projectStatus,
            @Param("recruitStack") TechStack recruitStack,
            @Param("projectStack") TechStack projectStack,
            @Param("platformType") PlatformType platformType,
            @Param("sort") BoardSortType sort,
            Pageable pageable
    );
}