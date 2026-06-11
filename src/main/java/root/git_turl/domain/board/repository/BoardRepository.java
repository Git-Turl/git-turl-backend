package root.git_turl.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.*;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByBoardType(BoardType boardType, Pageable pageable);

    @Query(
            value = """
    SELECT b AS board, COUNT(DISTINCT bl.id) AS likeCount
    FROM Board b
    LEFT JOIN BoardLike bl ON bl.board = b
    WHERE (:boardType IS NULL OR b.boardType = :boardType)
      AND (:studyTag IS NULL OR b.studyTag = :studyTag)
      AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
      AND (:recruitStack IS NULL OR :recruitStack MEMBER OF b.recruitStacks)
      AND (:projectStack IS NULL OR :projectStack MEMBER OF b.projectStacks)
      AND (:platformType IS NULL OR :platformType MEMBER OF b.platformTypes)
    GROUP BY b
    ORDER BY b.createdAt DESC
""",
            countQuery = """
    SELECT COUNT(DISTINCT b)
    FROM Board b
    WHERE (:boardType IS NULL OR b.boardType = :boardType)
      AND (:studyTag IS NULL OR b.studyTag = :studyTag)
      AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
      AND (:recruitStack IS NULL OR :recruitStack MEMBER OF b.recruitStacks)
      AND (:projectStack IS NULL OR :projectStack MEMBER OF b.projectStacks)
      AND (:platformType IS NULL OR :platformType MEMBER OF b.platformTypes)
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
    LEFT JOIN BoardLike bl ON bl.board = b
    WHERE (:boardType IS NULL OR b.boardType = :boardType)
      AND (:studyTag IS NULL OR b.studyTag = :studyTag)
      AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
      AND (:recruitStack IS NULL OR :recruitStack MEMBER OF b.recruitStacks)
      AND (:projectStack IS NULL OR :projectStack MEMBER OF b.projectStacks)
      AND (:platformType IS NULL OR :platformType MEMBER OF b.platformTypes)
    GROUP BY b
    ORDER BY COUNT(DISTINCT bl.id) DESC, b.createdAt DESC
""",
            countQuery = """
    SELECT COUNT(DISTINCT b)
    FROM Board b
    WHERE (:boardType IS NULL OR b.boardType = :boardType)
      AND (:studyTag IS NULL OR b.studyTag = :studyTag)
      AND (:projectStatus IS NULL OR b.projectStatus = :projectStatus)
      AND (:recruitStack IS NULL OR :recruitStack MEMBER OF b.recruitStacks)
      AND (:projectStack IS NULL OR :projectStack MEMBER OF b.projectStacks)
      AND (:platformType IS NULL OR :platformType MEMBER OF b.platformTypes)
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

    @Query(
            value = """
    SELECT b AS board, COUNT(DISTINCT bl.id) AS likeCount
    FROM Board b
    LEFT JOIN BoardLike bl ON bl.board = b
    WHERE b.member.id = :memberId
    GROUP BY b
    ORDER BY b.createdAt DESC
""",
            countQuery = """
    SELECT COUNT(b)
    FROM Board b
    WHERE b.member.id = :memberId
"""
    )
    Page<BoardPreviewProjection> findBoardsByMemberIdOrderByLatest(
            @Param("memberId") Long memberId,
            Pageable pageable
    );

    @Query("""
    SELECT b AS board, COUNT(DISTINCT bl.id) AS likeCount
    FROM Board b
    LEFT JOIN b.recruitStacks rs
    LEFT JOIN b.projectStacks ps
    LEFT JOIN BoardLike bl ON bl.board = b
    WHERE b.boardType = root.git_turl.domain.board.enums.BoardType.PROJECT
      AND (
          rs IN (:techStacks)
          OR ps IN (:techStacks)
      )
    GROUP BY b
    ORDER BY b.createdAt DESC
""")
    List<BoardPreviewProjection> findRecommendedProjectsByTechStacks(
            @Param("techStacks") List<TechStack> techStacks,
            Pageable pageable
    );

    @Query("""
    SELECT b AS board, COUNT(DISTINCT bl.id) AS likeCount
    FROM Board b
    LEFT JOIN BoardLike bl ON bl.board = b
    WHERE b.boardType = root.git_turl.domain.board.enums.BoardType.PROJECT
    GROUP BY b
    ORDER BY FUNCTION('RAND')
""")
    List<BoardPreviewProjection> findRandomProjects(Pageable pageable);
}