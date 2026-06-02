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

    @Query(
            value = """
        SELECT b AS board, COUNT(DISTINCT bl.id) AS likeCount
        FROM Board b
        JOIN b.member m
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
        ORDER BY b.createdAt DESC
    """,
            countQuery = """
        SELECT COUNT(DISTINCT b)
        FROM Board b
        LEFT JOIN b.recruitStacks rs
        LEFT JOIN b.projectStacks ps
        LEFT JOIN b.platformTypes pt
        WHERE (:boardType IS NULL OR b.boardType = :boardType)
          AND (:studyTag IS NULL OR b.studyTag = :studyTag)
          AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
          AND (:recruitStack IS NULL OR rs = :recruitStack)
          AND (:projectStack IS NULL OR ps = :projectStack)
          AND (:platformType IS NULL OR pt = :platformType)
    """
    )
    Page<BoardPreviewProjection> findBoardListWithFiltersOrderByLatest(
            @Param("boardType") BoardType boardType,
            @Param("studyTag") StudyTag studyTag,
            @Param("projectStatus") ProjectStatus projectStatus,
            @Param("recruitStack") TechStack recruitStack,
            @Param("projectStack") TechStack projectStack,
            @Param("platformType") PlatformType platformType,
            Pageable pageable
    );

    @Query(
            value = """
        SELECT b AS board, COUNT(DISTINCT bl.id) AS likeCount
        FROM Board b
        JOIN FETCH b.member m
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
        ORDER BY COUNT(DISTINCT bl.id) DESC, b.createdAt DESC
    """,
            countQuery = """
        SELECT COUNT(DISTINCT b)
        FROM Board b
        LEFT JOIN b.recruitStacks rs
        LEFT JOIN b.projectStacks ps
        LEFT JOIN b.platformTypes pt
        WHERE (:boardType IS NULL OR b.boardType = :boardType)
          AND (:studyTag IS NULL OR b.studyTag = :studyTag)
          AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
          AND (:recruitStack IS NULL OR rs = :recruitStack)
          AND (:projectStack IS NULL OR ps = :projectStack)
          AND (:platformType IS NULL OR pt = :platformType)
    """
    )
    Page<BoardPreviewProjection> findBoardListWithFiltersOrderByLikeCount(
            @Param("boardType") BoardType boardType,
            @Param("studyTag") StudyTag studyTag,
            @Param("projectStatus") ProjectStatus projectStatus,
            @Param("recruitStack") TechStack recruitStack,
            @Param("projectStack") TechStack projectStack,
            @Param("platformType") PlatformType platformType,
            Pageable pageable
    );
}