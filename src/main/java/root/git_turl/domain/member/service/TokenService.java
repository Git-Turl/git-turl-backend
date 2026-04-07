package root.git_turl.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.member.dto.TokenDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.entity.RefreshToken;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.domain.member.repository.RefreshTokenRepository;
import root.git_turl.global.security.jwt.JwtUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    // 토큰 발급
    @Transactional
    public TokenDto.Tokens issueTokens(Member member) {
        String accessToken = jwtUtil.createAccessToken(member);
        String refreshTokenValue = jwtUtil.createRefreshToken(member);
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .map(rt -> {
                    rt.update(refreshTokenValue, LocalDateTime.now().plusDays(1));
                    return rt;
                })
                .orElse(
                        new RefreshToken(refreshTokenValue, member, LocalDateTime.now().plusDays(1))
                );

        refreshTokenRepository.save(refreshToken);

        return new TokenDto.Tokens(accessToken, refreshTokenValue);
    }
}
