package root.git_turl.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.converter.MemberConverter;
import root.git_turl.domain.member.dto.MemberReqDto;
import root.git_turl.domain.member.dto.MemberResDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResDto.ProfileImage getProfileImage(Member member) {
        return MemberConverter.ProfileImage(member.getProfileImage());
    }

    @Transactional
    public void saveProfile(Member currentMember, MemberReqDto.Onboarding dto) {
        if (dto.getNickname() == null || dto.getJobType()==null || dto.getTechStackList()==null) {
            throw new MemberException(MemberErrorCode.PROFILE_BAD_REQUEST);
        }

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        member.saveProfile(dto.getNickname(), dto.getProfileImage(), dto.getJobType(), dto.getTechStackList());
    }
}
