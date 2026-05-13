package root.git_turl.domain.member.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.member.code.MemberSuccessCode;
import root.git_turl.domain.member.dto.MemberReqDto;
import root.git_turl.domain.member.dto.MemberResDto;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.service.MemberService;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController implements MemberControllerDocs{

    private final MemberService memberService;

    @GetMapping("/me/profile-image")
    public ApiResponse<MemberResDto.ProfileImage> getProfileImage (
            @CurrentUser @Parameter(hidden = true) Member member
    ) {
        return ApiResponse.onSuccess(MemberSuccessCode.PROFILE_IMAGE_GET_OK, memberService.getProfileImage(member));
    }

    @PatchMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> updateProfileImage (
            @CurrentUser @Parameter(hidden = true) Member member,
            @ModelAttribute MemberReqDto.ProfileImage profileImage
    ) {
        memberService.updateProfileImage(member, profileImage);
        return ApiResponse.onSuccess(MemberSuccessCode.PROFILE_IMAGE_PATCH_OK, null);
    }

    @PatchMapping("/me/onboarding")
    public ApiResponse<Void> saveProfile (
            @CurrentUser @Parameter(hidden = true) Member member,
            @Valid @RequestBody MemberReqDto.Onboarding request
    ) {
        memberService.saveProfile(member, request);
        return ApiResponse.onSuccess(MemberSuccessCode.ONBOARDING_PATCH_OK, null);
    }

    @PatchMapping("/me/profile")
    public ApiResponse<Void> updateProfile (
            @CurrentUser @Parameter(hidden = true) Member member,
            @Valid @RequestBody MemberReqDto.ProfileInfo request
    ) {
        memberService.updateProfile(member, request);
        return ApiResponse.onSuccess(MemberSuccessCode.PROFILE_INFO_PATCH_OK, null);
    }

    @GetMapping("/me/profile")
    public ApiResponse<MemberResDto.ProfileInfo> getMyProfile (
            @CurrentUser @Parameter(hidden = true) Member member
    ) {
        return ApiResponse.onSuccess(MemberSuccessCode.PROFILE_INFO_GET_OK,  memberService.getMyProfile(member));
    }

    @GetMapping("/{memberId}/profile")
    public ApiResponse<MemberResDto.ProfileInfo> getMemberProfile (
            @PathVariable Long memberId
    ) {
        return ApiResponse.onSuccess(MemberSuccessCode.PROFILE_INFO_GET_OK,  memberService.getMemberProfile(memberId));
    }

    @PostMapping("/nickname")
    public ApiResponse<Void> nicknameDuplicateCheck(
            @CurrentUser @Parameter(hidden = true) Member member,
            @RequestBody @Valid MemberReqDto.Nickname dto
    ) {
        boolean isDuplicated = memberService.nicknameDuplicateCheck(member, dto.getNickname());
        if (isDuplicated) {
            return ApiResponse.onSuccess(MemberSuccessCode.NICKNAME_CHECK_DUPLICATED, null);
        }
        return ApiResponse.onSuccess(MemberSuccessCode.NICKNAME_CHECK_OK, null);
    }
}
