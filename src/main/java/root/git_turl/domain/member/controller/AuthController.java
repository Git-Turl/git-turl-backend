package root.git_turl.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.git_turl.domain.member.code.AuthSuccessCode;
import root.git_turl.domain.member.dto.TokenDto;
import root.git_turl.domain.member.service.TokenService;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController implements AuthControllerDocs{

    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ApiResponse<TokenDto.AccessToken> accessTokenReissue(
            @CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response
    ) {
        return ApiResponse.onSuccess(AuthSuccessCode.TOKEN_REISSUE_OK, tokenService.reissueTokens(refreshToken, response));
    }
}
