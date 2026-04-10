package root.git_turl.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import root.git_turl.domain.member.enums.JobType;
import root.git_turl.domain.member.enums.TechStack;

import java.util.List;

public class MemberReqDto {

    @Getter
    public static class Onboarding {
        @NotNull
        @Size(max=20)
        private String nickname;

        private String profileImage;

        @NotNull
        private JobType jobType;

        @NotNull
        private List<TechStack> techStackList;

    }
}
