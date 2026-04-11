package root.git_turl.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.member.enums.JobType;
import root.git_turl.domain.member.enums.TechStack;

import java.util.List;

public class MemberReqDto {

    @Getter
    public static class Onboarding {
        @NotNull
        @Size(max=20)
        private String nickname;

        @NotNull
        private JobType jobType;

        @NotNull
        private List<TechStack> techStackList;

    }

    @Data
    public static class ProfileImage {
        @NotNull
        MultipartFile profileImage;
    }
}
