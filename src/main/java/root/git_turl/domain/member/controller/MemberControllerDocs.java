package root.git_turl.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.member.dto.MemberReqDto;
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

    @Operation(
            summary = "프로필 이미지 수정",
            description = "프로필 이미지를 수정합니다."
    )
    public ApiResponse<Void> updateProfileImage (
            @CurrentUser @Parameter(hidden = true) Member member,
            @ModelAttribute MemberReqDto.ProfileImage profileImage
    );

    @Operation(
            summary = "프로필 정보 설정",
            description = "첫 가입 후 프로필 정보를 설정합니다."
    )
    public ApiResponse<Void> saveProfile (
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestBody MemberReqDto.Onboarding request
    );

    @Operation(
            summary = "프로필 정보 수정",
            description = "프로필 정보를 수정합니다."
    )
    public ApiResponse<Void> updateProfile (
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestBody @Valid MemberReqDto.ProfileInfo request
    );

    @Operation(
            summary = "내 프로필 정보 조회",
            description = "내 프로필 정보를 조회합니다."
    )
    public ApiResponse<MemberResDto.ProfileInfo> getMyProfile (
            @CurrentUser @Parameter(hidden = true) Member member
    );

    @Operation(
            summary = "타인 프로필 정보 조회",
            description = "타인 프로필 정보를 조회합니다."
    )
    public ApiResponse<MemberResDto.ProfileInfo> getMemberProfile (
            @PathVariable Long memberId
    );

    @Operation(
            summary = "닉네임 중복 확인",
            description = "닉네임 중복 여부를 확인합니다."
    )
    public ApiResponse<Void> nicknameDuplicateCheck(
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestBody @Valid MemberReqDto.Nickname dto
    );
}
