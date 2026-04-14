package root.git_turl.global.security.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.repository.MemberRepository;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuthService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuthMember = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuthMember.getAttributes();

        return saveOrUpdate(attributes);
    }

    private OAuthMember saveOrUpdate(Map<String, Object> attributes) {

        String socialUid = String.valueOf(attributes.get("id"));
        String login = (String) attributes.get("login");
        String profileImage = (String) attributes.get("avatar_url");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        return memberRepository.findBySocialUid(socialUid)
                .map(existingMember -> {
                    existingMember.updateGithubInfo(login, profileImage, name, email);
                    memberRepository.save(existingMember);
                    return new OAuthMember(existingMember, false);
                })
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .socialUid(socialUid)
                            .githubId(login)
                            .profileImage(profileImage)
                            .email(email)
                            .githubName(name)
                            .build();

                    Member savedMember = memberRepository.save(newMember);
                    return new OAuthMember(savedMember, true);
                });
    }
}