package root.git_turl.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import root.git_turl.domain.member.dto.TokenDto;
import root.git_turl.global.apiPayload.ApiResponse;

public interface AuthControllerDocs {

    @Operation(
            summary = "토큰 재발급",
            description = "accessToken을 재발급하고, refreshToken을 갱신합니다."
    )
    public ApiResponse<TokenDto.AccessToken> accessTokenReissue(
            @CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response
    );
}
