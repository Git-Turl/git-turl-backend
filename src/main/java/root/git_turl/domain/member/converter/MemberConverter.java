package root.git_turl.domain.member.converter;

import root.git_turl.domain.member.dto.MemberResDto;
import root.git_turl.domain.member.entity.Member;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
                .githubId(member.getGithubId())
                .jobType(member.getJobType())
                .techStackList(member.getInterestStacks()
                        .stream()
                        .map(interestStack -> interestStack.getTechStack())
                        .toList())
                .build();
    }

    public static MemberResDto.History toHistory(
            Member member,
            long reportCount,
            long questionCount
    ) {
        return MemberResDto.History.builder()
                .daysWthGitTurl(ChronoUnit.DAYS.between(member.getCreatedAt().toLocalDate(), LocalDate.now()))
                .githubReportCount(reportCount)
                .interviewQuestionCount(questionCount)
                .build();
    }
}
