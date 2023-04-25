package whyzpotato.gamjacamp.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import whyzpotato.gamjacamp.config.auth.dto.OAuthAttributes;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.MemberRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // DefaultOAuth 와 delegate(대리자)를 통해 UserInfo 엔드포인트에서 사용자 정보를 가져온다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Member 생성 정보 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //google, kakao, naver
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //oauth 로그인 시 키가 되는 필드값
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        log.info("Login : ", registrationId, " ", userNameAttributeName);

        // 저장 (GUEST)
        Member member = saveOrUpdate(attributes);

        // 세션에 멤버 정보 유지
        httpSession.setAttribute("member", new SessionMember(member));

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(member.getRole().toString())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    public Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByAccount(attributes.getEmail())
                .orElse(attributes.toEntity());
        return memberRepository.save(member);

    }
}
