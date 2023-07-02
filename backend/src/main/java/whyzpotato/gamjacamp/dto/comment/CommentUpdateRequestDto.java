package whyzpotato.gamjacamp.dto.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {
    private String content;

    @Builder
    public CommentUpdateRequestDto(String content) {
        this.content = content;
    }
}
