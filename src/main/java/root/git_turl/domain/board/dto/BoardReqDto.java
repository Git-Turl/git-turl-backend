package root.git_turl.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
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

        //private MultipartFile boardImage;
    }

    @Data
    public static class UpdateDto {

        private String title;

        private String content;

        private BoardType boardType;

        //private MultipartFile boardImage;
    }
}
