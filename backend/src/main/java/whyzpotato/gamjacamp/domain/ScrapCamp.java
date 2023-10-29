package whyzpotato.gamjacamp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ScrapCamp {

    @Id
    @GeneratedValue
    @Column(name = "scrap_camp_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    public ScrapCamp(Member member, Camp camp) {
        this.member = member;
        this.camp = camp;
    }

}
