package root.git_turl.domain.member.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements BaseSuccessCode {

    PROFILE_IMAGE_GET_OK(HttpStatus.OK,
            "MEMBER200_6",
            "프로필 사진이 조회되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
