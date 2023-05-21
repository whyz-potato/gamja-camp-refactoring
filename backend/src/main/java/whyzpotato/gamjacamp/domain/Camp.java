package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="camp")
public class Camp {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="memberId")
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private String campIntroduction;

    @Column(nullable = false)
    private Float campX;

    @Column(nullable = false)
    private Float campY;

    @Column
    private Time campOperationStart;

    @Column
    private Time campOperationEnd;

    @OneToMany(mappedBy="camp")
    private List<Room> rooms = new ArrayList<Room>();

    // reviews
    // images

    @Builder
    public Camp(Member member, String name, String address, Float campX, Float campY) {
        this.member = member;
        this.name = name;
        this.address = address;
        this.campX = campX;
        this.campY = campY;
    }

}
