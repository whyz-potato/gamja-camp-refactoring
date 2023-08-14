package whyzpotato.gamjacamp.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {

    GUEST("ROLE_GUEST"),
    CUSTOMER("ROLE_CUSTOMER"),
    OWNER("ROLE_OWNER"),
    ADMIN("ROLE_ADMIN");

    private final String key;
}
