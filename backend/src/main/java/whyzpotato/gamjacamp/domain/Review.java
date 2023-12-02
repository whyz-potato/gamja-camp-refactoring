package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.controller.dto.review.ReviewUpdateRequestDto;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    @Min(1) @Max(5)
    private int rate;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "review")
    private List<Image> images = new ArrayList<Image>();

    @Builder
    public Review(Member writer, Camp camp, Reservation reservation, String content, List<Image> images) {
        this.writer = writer;
        this.camp = camp;
        this.reservation = reservation;
        this.content = content;
        this.images = images;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
        if (!camp.getReviews().contains(this)) {
            //camp.getReviews().add(this);
        }
    }

    public Review update(ReviewUpdateRequestDto reviewUpdateRequestDto) {
        this.content = reviewUpdateRequestDto.getContent();
        return this;
    }

}
