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

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String username;

    private String picture;

    //TODO 전화번호 할 지 의논

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
