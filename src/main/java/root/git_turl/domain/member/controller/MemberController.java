package root.git_turl.domain.member.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.git_turl.domain.member.code.MemberSuccessCode;
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
}
