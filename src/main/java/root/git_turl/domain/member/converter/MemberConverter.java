package root.git_turl.domain.member.converter;

import root.git_turl.domain.member.dto.MemberResDto;
import root.git_turl.domain.member.entity.Member;

public class MemberConverter {

    public static MemberResDto.ProfileImage ProfileImage(
            String profileImage
    ) {
        return MemberResDto.ProfileImage.builder()
                .profileImage(profileImage)
                .build();
    }

    public static MemberResDto.ProfileInfo ProfileInfo(
            Member member
    ) {
        return MemberResDto.ProfileInfo.builder()
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .jobType(member.getJobType())
                .techStackList(member.getInterestStacks()
                        .stream()
                        .map(interestStack -> interestStack.getTechStack())
                        .toList())
                .build();
    }
}
