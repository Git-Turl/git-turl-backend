package root.git_turl.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.board.enums.BoardType;

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
        private String authorName;
        private Integer views;
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
        private String writerName;
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
}
