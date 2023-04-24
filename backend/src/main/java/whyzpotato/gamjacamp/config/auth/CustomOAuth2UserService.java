package whyzpotato.gamjacamp.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import whyzpotato.gamjacamp.config.auth.dto.OAuthAttributes;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.MemberRepository;

import javax.servlet.http.HttpSession;

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

        //
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //google, kakao, naver
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //oauth 로그인 시 키가 되는 필드값
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 저장 (GUEST)
        Member member = saveOrUpdate(attributes);

        // TODO 세션 유저

        return oAuth2User;
    }

    public Member saveOrUpdate(OAuthAttributes attributes){
        //TODO : 이미 있는 프로필이면 수정
        Member member = memberRepository.findByAccount(attributes.getEmail())
                .orElse(attributes.toEntity());
        return memberRepository.save(member);

    }
}
