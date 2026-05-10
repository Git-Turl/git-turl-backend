package root.git_turl.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import root.git_turl.domain.board.enums.BoardType;

public class BoardReqDto {

    @Getter
    public static class CreateBoardReqDto {

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        @NotNull
        private BoardType boardType;
    }
}
