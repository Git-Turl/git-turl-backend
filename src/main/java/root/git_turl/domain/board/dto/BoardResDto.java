package root.git_turl.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.board.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BoardResDto {

    @Getter
    @Builder
    public static class BoardDetailDto {

        private Long boardId;

        private String title;

        private String content;

        private String imageUrl;

        private BoardType boardType;

        // ===== 모집 공통 =====
        private Integer recruitCount;

        private LocalDate recruitDeadline;

        // ===== 스터디 게시판 =====
        private StudyTag studyTag;

        private CertificateType certificateType;

        // ===== 프로젝트 게시판 =====
        private ProjectStatus projectStatus;

        private List<TechStack> recruitStacks;

        private List<TechStack> projectStacks;

        private List<PlatformType> platformTypes;

        private String authorName;

        private Integer views;

        private Long likeCount;

        private Boolean isLiked;

        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class BoardCreateResultDto {

        private Long boardId;

        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class BoardUpdateResultDto {

        private Long boardId;

        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class BoardDeleteResultDto {

        private Long boardId;

        private LocalDateTime deletedAt;
    }

    @Getter
    @Builder
    public static class BoardPreviewDto {

        private Long boardId;

        private String title;

        private String content;

        private String imageUrl;

        private BoardType boardType;

        // ===== 모집 공통 =====
        private Integer recruitCount;

        private LocalDate recruitDeadline;

        // ===== 스터디 게시판 =====
        private StudyTag studyTag;

        private CertificateType certificateType;

        // ===== 프로젝트 게시판 =====
        private ProjectStatus projectStatus;

        private List<TechStack> recruitStacks;

        private List<TechStack> projectStacks;

        private List<PlatformType> platformTypes;

        private String writerName;

        private Long likeCount;

        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class BoardPreviewListDto {

        private List<BoardPreviewDto> boardList;

        private Integer listSize;

        private Integer totalPage;

        private Long totalElements;

        private Boolean isFirst;

        private Boolean isLast;
    }

    @Getter
    @Builder
    public static class RecommendProjectDto {

        private Long boardId;

        private String title;
        private String content;

        private List<TechStack> recruitStacks;

        private Long likeCount;
        private Integer views;
        private Integer recruitCount;
    }
}