package whyzpotato.gamjacamp.dto.review;

import lombok.Builder;
import org.springframework.data.domain.Page;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.Review;
import whyzpotato.gamjacamp.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

public class SimpleReviewResponseDto {

    private Long id;
    private Member writer;
    private String content;
    private List<Image> images = new ArrayList<Image>();

    @Builder
    public SimpleReviewResponseDto(Long id, Member writer, String content, List<Image> images) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.images = images;
    }

    public SimpleReviewResponseDto() {
    }

    public SimpleReviewResponseDto(Review review) {
        this.id = review.getId();
        this.writer = review.getWriter();
        this.content = review.getContent();
        this.images = review.getImages();
    }

    public Page<SimpleReviewResponseDto> toDtoList(Page<Review> reviews) {
        Page<SimpleReviewResponseDto> simpleReviewResponseDtoList = reviews.map(
                m -> SimpleReviewResponseDto.builder()
                        .id(m.getId())
                        .writer(m.getWriter())
                        .content(m.getContent())
                        .images(m.getImages())
                        .build()
                );
        return simpleReviewResponseDtoList;
    }
}
