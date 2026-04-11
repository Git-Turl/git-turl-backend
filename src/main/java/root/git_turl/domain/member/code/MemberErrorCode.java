package root.git_turl.domain.member.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import root.git_turl.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND,
            "MEMBER404_1",
            "존재하지 않는 사용자입니다."),

    PROFILE_BAD_REQUEST(HttpStatus.BAD_REQUEST,
            "MEMBER400_1",
            "필수 입력 항목이 입력되지 않았습니다."),

    NO_EDIT(HttpStatus.BAD_REQUEST,
            "MEMBER400_2",
            "수정 사항이 없습니다."),

    FILE_TYPE_ERROR(HttpStatus.BAD_REQUEST,
                "FILE400",
                    "이미지 파일만 업로드 가능합니다."),

    IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,
                "FILE404",
                    "프로필 이미지 업로드에 실패하였습니다."
    );

    private final HttpStatus status;
    private final String code;
    private final String message;
}
