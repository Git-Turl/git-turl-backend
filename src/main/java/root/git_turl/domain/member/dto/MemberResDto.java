package root.git_turl.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.member.enums.JobType;
import root.git_turl.domain.member.enums.TechStack;

import java.util.List;

public class MemberResDto {

    @Getter
    @Builder
    public static class ProfileImage {
        private String profileImage;
    }

    @Getter
    @Builder
    public static class ProfileInfo {
        private String nickname;
        private String githubId;
        private String profileImage;
        private JobType jobType;
        private List<TechStack> techStackList;
    }
}
