package root.git_turl.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.member.converter.MemberConverter;
import root.git_turl.domain.member.dto.MemberResDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResDto.ProfileImage getProfileImage(Member member) {
        return MemberConverter.ProfileImage(member.getProfileImage());
    }

}
