package root.git_turl.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TokenDto {

    @Getter
    @AllArgsConstructor
    public static class Tokens {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    public static class AccessToken {
        private String accessToken;
    }
}
