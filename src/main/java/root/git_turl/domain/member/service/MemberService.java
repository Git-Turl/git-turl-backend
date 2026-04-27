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
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.global.aws.AwsFileService;
import root.git_turl.infrastructure.github.GithubClient;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AwsFileService awsFileService;
    private final GithubClient githubClient;

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

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

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

        String email = githubClient.getEmail(currentMember.getGithubAccessToken());

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        member.saveProfile(dto.getNickname(), dto.getJobType(), dto.getTechStackList());
        member.setEmail(email);
    }

    @Transactional
    public void updateProfile(Member currentMember, MemberReqDto.ProfileInfo dto) {
        if (dto.getNickname() == null && dto.getJobType()==null && dto.getTechStackList()==null) {
            throw new MemberException(MemberErrorCode.NO_EDIT);
        }

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        member.updateProfile(dto.getNickname(), dto.getJobType(), dto.getTechStackList());
    }

    @Transactional(readOnly = true)
    public MemberResDto.ProfileInfo getMyProfile(Member currentMember) {
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        return MemberConverter.ProfileInfo(member);
    }

    @Transactional(readOnly = true)
    public MemberResDto.ProfileInfo getMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        return MemberConverter.ProfileInfo(member);
    }
}
