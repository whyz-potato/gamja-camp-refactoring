package whyzpotato.gamjacamp.dto.Post;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GeneralPostDto {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private PostType postType;
    private List<Image> images;

    @Builder
    public GeneralPostDto(Long id, Long memberId, String title, String content, PostType postType, List<Image> images) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.images = images;
    }

    public GeneralPostDto(Post post) {
        this.id = post.getId();
        this.memberId = post.getWriter().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postType = post.getType();
        this.images = post.getImages();
    }

}
