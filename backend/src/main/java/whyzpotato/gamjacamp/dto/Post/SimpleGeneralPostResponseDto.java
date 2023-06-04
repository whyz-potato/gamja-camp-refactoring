package whyzpotato.gamjacamp.dto.Post;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.post.Post;

import java.util.List;

@Getter
public class SimpleGeneralPostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Image image;

    @Builder
    public SimpleGeneralPostResponseDto(Long id, String title, String content, List<Image> images) {
        this.id = id;
        this.title = title;
        this.content = content;
        if (images != null)
            this.image = images.get(0);   //TODO 첫번째 사진
    }

    public SimpleGeneralPostResponseDto() {

    }

    public SimpleGeneralPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        if (post.getImages() != null)
            this.image = post.getImages().get(0);   //TODO 첫번째 사진
    }

    public Page<SimpleGeneralPostResponseDto> toDtoList(Page<Post> posts) {
        Page<SimpleGeneralPostResponseDto> simpleGeneralPostResponseDtoList = posts.map(
                m -> SimpleGeneralPostResponseDto.builder()
                        .id(m.getId())
                        .title(m.getTitle())
                        .content(m.getContent())
                        .images(m.getImages())
                        .build());
        return simpleGeneralPostResponseDtoList;
    }
}
