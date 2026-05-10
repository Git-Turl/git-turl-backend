package root.git_turl.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.board.enums.BoardType;

import java.time.LocalDateTime;

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
}
