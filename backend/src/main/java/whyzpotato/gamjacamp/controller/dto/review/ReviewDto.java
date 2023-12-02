package whyzpotato.gamjacamp.controller.dto.review;

import lombok.Builder;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Review;
import whyzpotato.gamjacamp.domain.member.Member;

import java.util.List;

public class ReviewDto {

    private Long id;
    private Member writer;
    private Camp camp;
    private Reservation reservation;
    private String content;
    private List<Image> images;

    @Builder
    public ReviewDto(Long id, Member writer, Camp camp, Reservation reservation, String content, List<Image> images) {
        this.id = id;
        this.writer = writer;
        this.camp = camp;
        this.reservation = reservation;
        this.content = content;
        this.images = images;
    }

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.writer = review.getWriter();
        this.camp = review.getCamp();
        this.reservation = review.getReservation();
        this.content = review.getContent();
        this.images = review.getImages();
    }
}
