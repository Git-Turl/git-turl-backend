package root.git_turl.domain.member.converter;

import root.git_turl.domain.member.dto.MemberResDto;

public class MemberConverter {

    public static MemberResDto.ProfileImage ProfileImage(
            String profileImage
    ) {
        return MemberResDto.ProfileImage.builder()
                .profileImage(profileImage)
                .build();
    }
}
