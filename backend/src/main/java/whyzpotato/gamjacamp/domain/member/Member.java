package whyzpotato.gamjacamp.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id @GeneratedValue
    private Long memberId;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String username;

    @Column
    private String picture;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String account, String username, String picture, Role role) {
        this.account = account;
        this.username = username;
        this.picture = picture;
        this.role = role;
    }

}
