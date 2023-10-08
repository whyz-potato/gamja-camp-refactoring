package whyzpotato.gamjacamp.dto.review;

import lombok.Getter;

@Getter
public class ReviewUpdateRequestDto {
    private String content;

    public ReviewUpdateRequestDto(String content) {
        this.content = content;
    }
}
