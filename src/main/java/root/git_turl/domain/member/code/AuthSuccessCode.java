package root.git_turl.domain.member.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements BaseSuccessCode {

    TOKEN_REISSUE_OK(HttpStatus.OK,
            "AUTH200_1",
            "토큰이 재발급되었습니다."),

    LOGOUT_OK(HttpStatus.OK,
            "AUTH200_2",
            "로그아웃 되었습니다."),

    WITHDRAW_OK(HttpStatus.OK,
            "AUTH200_3",
            "회원탈퇴 되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
