package root.git_turl.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

public class MemberResDto {

    @Getter
    @Builder
    public static class ProfileImage {
        private String profileImage;
    }
}
