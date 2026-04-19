package root.git_turl.global.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import root.git_turl.domain.member.dto.TokenDto;
import root.git_turl.domain.member.service.TokenService;
import root.git_turl.global.security.CookieUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuthMember member = (OAuthMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TokenDto.Tokens tokenDto = tokenService.issueTokens(member.getMember());
        CookieUtil.addCookie(response, "refreshToken", tokenDto.getRefreshToken());

        OAuthMember oAuthMember = (OAuthMember) authentication.getPrincipal();
        String accessToken = tokenDto.getAccessToken();

        if (oAuthMember.isNew()) {
            response.sendRedirect("http://localhost:5173/login/loading?route=signup&token=" + accessToken);
        } else {
            response.sendRedirect("http://localhost:5173/login/loading?route=home&token=" + accessToken);
        }
    }
}