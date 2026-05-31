package root.git_turl.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import root.git_turl.domain.board.enums.*;

import java.time.LocalDate;
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

        // ===== 모집 공통 =====
        private Integer recruitCount;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recruitDeadline;

        // ===== 스터디 게시판 =====
        private StudyTag studyTag;

        private CertificateType certificateType;

        // ===== 프로젝트 게시판 =====
        private ProjectStatus projectStatus;

        private List<TechStack> recruitStacks;

        private List<TechStack> projectStacks;

        private List<PlatformType> platformTypes;
    }

    @Data
    public static class UpdateDto {

        private String title;

        private String content;

        private BoardType boardType;

        // ===== 모집 공통 =====
        private Integer recruitCount;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recruitDeadline;

        // ===== 스터디 게시판 =====
        private StudyTag studyTag;

        private CertificateType certificateType;

        // ===== 프로젝트 게시판 =====
        private ProjectStatus projectStatus;

        private List<TechStack> recruitStacks;

        private List<TechStack> projectStacks;

        private List<PlatformType> platformTypes;
    }
}