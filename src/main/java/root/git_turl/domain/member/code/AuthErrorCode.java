package root.git_turl.domain.member.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    NO_REFRESH_TOKEN(
            HttpStatus.NOT_FOUND,
            "AUTH404_1",
            "리프레시 토큰이 존재하지 않습니다."
    ),

    INVALID_REFRESH_TOKEN(
            HttpStatus.UNAUTHORIZED,
            "AUTH401_1",
            "유효하지 않은 리프레시 토큰입니다."
    ),

    EXPIRED_REFRESH_TOKEN(
            HttpStatus.UNAUTHORIZED,
            "AUTH401_2",
            "만료된 리프레시 토큰입니다."
    );

    private final HttpStatus status;
    private final String code;
    private final String message;
}
