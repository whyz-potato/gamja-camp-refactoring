package whyzpotato.gamjacamp.dto.Post;

import lombok.Builder;
import lombok.Getter;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GeneralPostSaveRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private List<Image> images = new ArrayList<Image>();

    @Builder
    public GeneralPostSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity(Member member, PostType postType) {
        return Post.builder()
                .writer(member)
                .title(this.title)
                .content(this.content)
                .type(postType)
                .images(this.images)
                .build();
    }
}
