package whyzpotato.gamjacamp.dto.Post;

import lombok.Getter;
import whyzpotato.gamjacamp.domain.Image;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GeneralPostUpdateRequestDto {
    private String title;
    private String content;
    private List<Image> images = new ArrayList<Image>();
}
