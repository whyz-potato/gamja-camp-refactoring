package whyzpotato.gamjacamp.domain.member;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Member {

    @Id @GeneratedValue
    private Long memberId;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String account, String password, String username, Role role) {
        this.account = account;
        this.password = password;
        this.username = username;
        this.role = role;
    }


}
