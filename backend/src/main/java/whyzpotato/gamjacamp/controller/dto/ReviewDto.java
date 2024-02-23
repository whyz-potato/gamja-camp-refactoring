package whyzpotato.gamjacamp.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Review;
import whyzpotato.gamjacamp.domain.member.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ReviewDto {

    @Getter
    @NoArgsConstructor
    public static class ReviewSimple {
        private Long id;
        private Long writer;
        private Long camp;
        private Long reservation;
        private int rate;

        @Builder
        public ReviewSimple(Long id, Long writer, Long camp, Long reservation, int rate) {
            this.id = id;
            this.writer = writer;
            this.camp = camp;
            this.reservation = reservation;
            this.rate = rate;
        }

        public Page<ReviewSimple> toList(Page<Review> reviews) {
            Page<ReviewSimple> reviewSimpleList = reviews.map(
                    m-> ReviewSimple.builder()
                            .id(m.getId())
                            .writer(m.getWriter().getId())
                            .camp(m.getCamp().getId())
                            .reservation(m.getReservation().getId())
                            .rate(m.getRate())
                            .build());
            return reviewSimpleList;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewDetail {
        private Long id;
        private Long writer;
        private Long camp;
        private Long reservation;
        private int rate;
        private String content;
        private List<String> images;

        @Builder
        public ReviewDetail(Long id, Member writer, Camp camp, Reservation reservation, int rate, String content, List<Image> images) {
            this.id = id;
            this.writer = writer.getId();
            this.camp = camp.getId();
            this.reservation = reservation.getId();
            this.rate = rate;
            this.content = content;
            if(!images.isEmpty()) {
                this.images = images.stream()
                        .map(Image::getPath)
                        .collect(Collectors.toList());
            }
        }

        public ReviewDetail(whyzpotato.gamjacamp.domain.Review review) {
            this.id = review.getId();
            this.writer = review.getWriter().getId();
            this.camp = review.getCamp().getId();
            this.reservation = review.getReservation().getId();
            this.rate = review.getRate();
            this.content = review.getContent();
            if(!review.getImages().isEmpty()) {
                this.images = review.getImages().stream()
                        .map(Image::getPath)
                        .collect(Collectors.toList());
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewSaveRequest {
        private int rate;
        private String content;

        @Builder
        public ReviewSaveRequest(int rate, String content) {
            this.rate = rate;
            this.content = content;
        }

        public whyzpotato.gamjacamp.domain.Review toEntity(Member member, Camp camp, Reservation reservation) {
            return whyzpotato.gamjacamp.domain.Review.builder()
                    .writer(member)
                    .camp(camp)
                    .reservation(reservation)
                    .rate(rate)
                    .content(content)
                    .images(new ArrayList<>())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewUpdateRequest {
        private int rate;
        private String content;

        @Builder
        public ReviewUpdateRequest(int rate, String content) {
            this.rate = rate;
            this.content = content;
        }
    }
}                                    
              
