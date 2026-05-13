package root.git_turl.domain.member.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseSuccessCode;

@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements BaseSuccessCode {

    ONBOARDING_PATCH_OK(HttpStatus.OK,
            "MEMBER200_1",
            "프로필이 저장되었습니다."),

    PROFILE_INFO_PATCH_OK(HttpStatus.OK,
            "MEMBER200_2",
            "프로필이 수정되었습니다."),

    PROFILE_INFO_GET_OK(HttpStatus.OK,
            "MEMBER200_3",
            "프로필 정보를 조회했습니다."),

    PROFILE_IMAGE_GET_OK(HttpStatus.OK,
            "MEMBER200_6",
            "프로필 사진이 조회되었습니다."),

    PROFILE_IMAGE_PATCH_OK(HttpStatus.OK,
            "MEMBER200_7",
            "프로필 사진이 수정되었습니다."),

    NICKNAME_CHECK_OK(HttpStatus.OK,
            "MEMBER200_8",
            "사용 가능한 닉네임입니다."),

    NICKNAME_CHECK_DUPLICATED(HttpStatus.OK,
            "MEMBER200_9",
            "사용 할 수 없는 닉네임입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
