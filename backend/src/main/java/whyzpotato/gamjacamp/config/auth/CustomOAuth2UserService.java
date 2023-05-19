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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 회원가입 구분 : 사장/고객
        String type = (String) Optional.ofNullable(httpSession.getAttribute("type"))
                .orElseThrow(() -> new IllegalStateException("잘못된 접근입니다."));

        // DefaultOAuth 와 delegate(대리자)를 통해 UserInfo 엔드포인트에서 사용자 정보를 가져온다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Provider가 준 정보 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //google, kakao, naver
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //oauth 로그인 시 키가 되는 필드값
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes(), type);

        // 가입된 회원 정보 가져오기 (없으면 회원가입)
        Member member = save(attributes);

        // 세션에 멤버 정보 유지
        httpSession.setAttribute("member", new SessionMember(member));
        // 더이상 필요없는 type 정보 삭제
        httpSession.removeAttribute("type"); //더이상 필요없는 정보 삭제

        log.info("로그인 완료 {} {}", member.getRole().name(), member.getAccount());

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(member.getRole().getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    public Member save(OAuthAttributes attributes) {
        Member member = memberRepository.findByAccount(attributes.getEmail()) //가입되어 있는 경우 기존 정보 가져오기
                .orElseGet(() -> memberRepository.save(attributes.toEntity())); //가입되지 않은 경우 회원가입
        return member;

    }
}
