package root.git_turl.domain.member.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.member.code.AuthSuccessCode;
import root.git_turl.domain.member.dto.TokenDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.service.MemberService;
import root.git_turl.domain.member.service.TokenService;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController implements AuthControllerDocs{

    private final TokenService tokenService;
    private final MemberService memberService;

    @GetMapping("/login/github")
    public void githubLogin(
            @RequestParam("redirect_uri") String redirectUri,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        request.getSession().setAttribute("redirect_uri", redirectUri);

        response.sendRedirect("/oauth2/authorization/github");
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenDto.AccessToken> accessTokenReissue(
            @CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response
    ) {
        return ApiResponse.onSuccess(AuthSuccessCode.TOKEN_REISSUE_OK, tokenService.reissueTokens(refreshToken, response));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        tokenService.logout(refreshToken);
        return ApiResponse.onSuccess(AuthSuccessCode.LOGOUT_OK, null);
    }

    @DeleteMapping("/withdraw")
    public ApiResponse<Void> withdraw(
            @CurrentUser @Parameter(hidden = true) Member member
    ) {
        memberService.withdraw(member);
        return ApiResponse.onSuccess(AuthSuccessCode.WITHDRAW_OK, null);
    }
}
