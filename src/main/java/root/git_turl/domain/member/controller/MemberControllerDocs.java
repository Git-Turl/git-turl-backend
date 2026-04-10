package root.git_turl.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import root.git_turl.domain.member.dto.MemberResDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

public interface MemberControllerDocs {

    @Operation(
            summary = "프로필 이미지 조회",
            description = "프로필 이미지 url을 반환합니다."
    )
    public ApiResponse<MemberResDto.ProfileImage> getProfileImage (
            @CurrentUser @Parameter(hidden = true) Member member
    );
}
