package root.git_turl.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import root.git_turl.domain.board.enums.*;

import java.util.List;

public class BoardReqDto {

    @Getter
    public static class CreateBoardReqDto {

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        @NotNull
        private BoardType boardType;

        // ===== 스터디 게시판 =====
        private StudyTag studyTag;

        // ===== 프로젝트 게시판 =====
        private ProjectStatus projectStatus;

        private List<TechField> techFields;

        private List<PlatformType> platformTypes;

        //private MultipartFile boardImage;
    }

    @Data
    public static class UpdateDto {

        private String title;

        private String content;

        private BoardType boardType;

        // ===== 스터디 게시판 =====
        private StudyTag studyTag;

        // ===== 프로젝트 게시판 =====
        private ProjectStatus projectStatus;

        private List<TechField> techFields;

        private List<PlatformType> platformTypes;

        //private MultipartFile boardImage;
    }
}
