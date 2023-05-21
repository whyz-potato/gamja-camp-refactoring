package whyzpotato.gamjacamp.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import java.util.Map;

/**
 * Oauth2User 의 정보 가져오기
 */

@Slf4j
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private Role role;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, Role role) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    // OAuth2 에서 가져온 Map 형태의 데이터를 dto에 담아 반환
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String role) {
        log.info("로그인 시도 {}", attributes.get("email"));

        if ("naver".equals(registrationId))
            return ofNaver("id", attributes, role);
        if ("kakao".equals(registrationId))
            return ofKakao("id", attributes, role);
        return ofGoogle(userNameAttributeName, attributes, role);
    }


    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String role) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .role(Role.valueOf(role))
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes, String role) {

        // 네이버로그인 API 명세 : response/{name, email, profile_image}
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .role(Role.valueOf(role))
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes, String role) {

        // 카카오로그인 API 명세 : kakao_account/profile/{profile_nickname, profile_image, account_email}
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) profile.get("thumbnail_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .role(Role.valueOf(role))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .username(name)
                .account(email)
                .picture(picture)
                .role(role)
                .build();
    }

}
