package whyzpotato.gamjacamp.dto.Post;

import lombok.Builder;
import lombok.Getter;
import whyzpotato.gamjacamp.domain.Image;

import java.util.List;

@Getter
public class GeneralPostUpdateRequestDto {
    private String title;
    private String content;

    @Builder
    public GeneralPostUpdateRequestDto(String title, String content, List<Image> images) {
        this.title = title;
        this.content = content;
    }
}
