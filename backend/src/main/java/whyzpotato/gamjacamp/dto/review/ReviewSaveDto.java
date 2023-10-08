package whyzpotato.gamjacamp.dto.review;

import lombok.Builder;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.Review;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class ReviewSaveDto {
    @NotBlank
    private String content;

    @Builder
    public ReviewSaveDto(String content) {
        this.content = content;
    }

    public Review toEntity(Member member) {
        return Review.builder()
                .writer(member)
                .content(this.content)
                .build();
    }

}
