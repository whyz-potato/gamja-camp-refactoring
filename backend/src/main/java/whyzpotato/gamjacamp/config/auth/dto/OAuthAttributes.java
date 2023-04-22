package whyzpotato.gamjacamp.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import java.util.Map;

/**
 *
 * */

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    // OAuth2 에서 가져온 Map 형태의 데이터를 dto에 담아 반환
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        return ofGoogle(userNameAttributeName, attributes);
    }


    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity(){
        return Member.builder()
                .username(name)
                .account(email)
                .role(Role.ROLE_GUEST)
                .build();
    }

    //TODO : 네이버 연동로그인
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        return null;
    }

}
