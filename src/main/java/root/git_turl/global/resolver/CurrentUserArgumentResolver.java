package root.git_turl.global.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.service.MemberService;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.code.GeneralErrorCode;
import root.git_turl.global.apiPayload.exception.GeneralException;
import root.git_turl.global.security.CustomUserDetails;

@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) &&
                parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 1. SecurityContext에서 인증 정보 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. 인증 정보가 없거나 principal이 UserDetails 타입이 아니면 예외
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. memberId로 DB에서 조회해서 반환
        Long memberId = ((CustomUserDetails) userDetails).getMember().getId();

        return ((CustomUserDetails) userDetails).getMember();
    }
}