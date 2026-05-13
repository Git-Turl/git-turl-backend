package root.git_turl.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.converter.MemberConverter;
import root.git_turl.domain.member.dto.MemberReqDto;
import root.git_turl.domain.member.dto.MemberResDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.enums.Status;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.aws.AwsFileService;
import root.git_turl.infrastructure.github.GithubClient;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AwsFileService awsFileService;
    private final GithubClient githubClient;
    private final ReportRepository reportRepository;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public MemberResDto.ProfileImage getProfileImage(Member member) {
        return MemberConverter.ProfileImage(member.getProfileImage());
    }

    @Transactional
    public void updateProfileImage(Member currentMember, MemberReqDto.ProfileImage profileImage) {
        MultipartFile image = profileImage.getProfileImage();
        if  (image == null) {
            throw new MemberException(MemberErrorCode.PROFILE_BAD_REQUEST);
        }

        Member member = getMember(currentMember.getId());

        try {
            String imageUrl = awsFileService.saveProfileImg(image, member.getId());
            member.updateProfileImage(imageUrl);

        } catch (IOException e) {
            throw new MemberException(MemberErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }

    @Transactional
    public void saveProfile(Member currentMember, MemberReqDto.Onboarding dto) {
        if (dto.getNickname() == null || dto.getJobType()==null || dto.getTechStackList()==null) {
            throw new MemberException(MemberErrorCode.PROFILE_BAD_REQUEST);
        }

        validateNicknameDuplicate(dto.getNickname());

        String email = githubClient.getEmail(currentMember.getGithubAccessToken());

        Member member = getMember(currentMember.getId());

        member.saveProfile(dto.getNickname(), dto.getJobType(), dto.getTechStackList());
        member.setEmail(email);
    }

    @Transactional
    public void updateProfile(Member currentMember, MemberReqDto.ProfileInfo dto) {
        if (dto.getNickname() == null && dto.getJobType()==null && dto.getTechStackList()==null) {
            throw new MemberException(MemberErrorCode.NO_EDIT);
        }

        validateNicknameDuplicate(dto.getNickname());

        Member member = getMember(currentMember.getId());

        member.updateProfile(dto.getNickname(), dto.getJobType(), dto.getTechStackList());
    }

    @Transactional(readOnly = true)
    public MemberResDto.ProfileInfo getMyProfile(Member currentMember) {
        Member member = getMember(currentMember.getId());
        return MemberConverter.ProfileInfo(member);
    }

    @Transactional(readOnly = true)
    public MemberResDto.ProfileInfo getMemberProfile(Long memberId) {
        Member member = getMember(memberId);
        return MemberConverter.ProfileInfo(member);
    }

    @Transactional
    public void withdraw(Member currentMember) {
        Member member = getMember(currentMember.getId());
        member.inactivateMember();
    }

    @Transactional(readOnly = true)
    public boolean nicknameDuplicateCheck(Member currentMember, String nickname) {
        return isNicknameDuplicate(nickname);
    }

    @Transactional(readOnly = true)
    public MemberResDto.History getHistory(Member currentMember) {
        Member member = getMember(currentMember.getId());
        long reportCount = reportRepository.countByMemberAndGenerationStatus(member, GenerationStatus.DONE);
        long questionCount = questionRepository.countByMemberAndStatus(member, GenerationStatus.DONE);
        return MemberConverter.toHistory(member, reportCount, questionCount);
    }

    private Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        return member;
    }

    private void validateNicknameDuplicate(String nickname) {
        if (isNicknameDuplicate(nickname)) {
            throw new MemberException(MemberErrorCode.NICKNAME_DUPLICATE);
        }
    }

    private boolean isNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
