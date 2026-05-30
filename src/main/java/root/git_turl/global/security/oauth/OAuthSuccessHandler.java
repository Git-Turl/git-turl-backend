package root.git_turl.global.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import root.git_turl.domain.member.dto.TokenDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.domain.member.service.TokenService;
import root.git_turl.global.security.CookieUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final OAuth2AuthorizedClientService clientService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 인증된 사용자 정보
        OAuthMember oAuthMember = (OAuthMember) authentication.getPrincipal();
        Member member = oAuthMember.getMember();


        // 2. GitHub Access Token
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );

        String githubAccessToken = client.getAccessToken().getTokenValue();


        member.updateGithubToken(githubAccessToken);
        memberRepository.save(member);


        // JWT 토큰 발급
        TokenDto.Tokens tokenDto = tokenService.issueTokens(member);

        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        CookieUtil.addCookie(response, "refreshToken", refreshToken);

        if (oAuthMember.isNew()) {
            response.sendRedirect("http://localhost:5173/login/loading?route=signup&token=" + accessToken);
        } else {
            response.sendRedirect("http://localhost:5173/login/loading?route=home&token=" + accessToken);
        }
    }
}