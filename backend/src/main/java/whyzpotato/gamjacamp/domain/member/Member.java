package whyzpotato.gamjacamp.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.ScrapCamp;
import whyzpotato.gamjacamp.domain.ScrapPost;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.post.Post;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //TODO : 감자캠핑에서 프로필 사진 수정 가능하므로 Image 로 저장
    private String picture;

    //TODO 전화번호 할 지 의논

    //작성한 글
    @OneToMany(mappedBy = "writer")
    private List<Post> posts = new ArrayList<Post>();

    //예약 내역
    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations = new ArrayList<Reservation>();

    //참여중인 채팅
    @OneToMany(mappedBy = "member")
    private List<ChatMember> chatMemberList = new ArrayList<ChatMember>();

    //스크랩
    @OneToMany(mappedBy = "member")
    private List<ScrapCamp> scrapCamps = new ArrayList<ScrapCamp>();

    //스크랩
    @OneToMany(mappedBy = "member")
    private List<ScrapPost> scrapPosts = new ArrayList<ScrapPost>();


    @Builder
    public Member(String account, String username, String picture, Role role) {
        this.account = account;
        this.username = username;
        this.picture = picture;
        this.role = role;
    }

}
