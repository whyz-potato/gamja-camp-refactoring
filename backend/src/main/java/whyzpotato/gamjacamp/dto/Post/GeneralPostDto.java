package whyzpotato.gamjacamp.dto.Post;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GeneralPostDto {

    //GeneralPostSimple
    @Getter
    @NoArgsConstructor
    public static class GeneralPostSimple {
        private Long id;
        private String title;
        private String content;
        private String image;

        @Builder
        public GeneralPostSimple(Long id, String title, String content, List<Image> images) {
            this.id = id;
            this.title = title;
            this.content = content;
            if (!images.isEmpty())
                this.image = images.get(0).getPath();   //TODO 첫번째 사진
        }

        public GeneralPostSimple(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            if (!post.getImages().isEmpty())
                this.image = post.getImages().get(0).getPath();   //TODO 첫번째 사진
        }

        public Page<GeneralPostSimple> toList(Page<Post> posts) {
            Page<GeneralPostSimple> generalPostSimpleList = posts.map(
                    m -> GeneralPostSimple.builder()
                            .id(m.getId())
                            .title(m.getTitle())
                            .content(m.getContent())
                            .images(m.getImages())
                            .build());
            return generalPostSimpleList;
        }
    }

    //GeneralPostDetail
    @Getter
    @NoArgsConstructor
    public static class GeneralPostDetail {
        private Long id;
//        private Member writer;
        private String title;
        private String content;
        private PostType postType;
        private List<String> images;

        @Builder
        public GeneralPostDetail(Long id, Member writer, String title, String content, PostType postType, List<Image> images, List<Comment> comments) {
            this.id = id;
//            this.writer = writer;
            this.title = title;
            this.content = content;
            this.postType = postType;
            if (!images.isEmpty()) {
                this.images = images.stream()
                        .map(Image::getPath)
                        .collect(Collectors.toList());
            }
        }

        public GeneralPostDetail(Post post) {
            this.id = post.getId();
//            this.writer = post.getWriter();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.postType = post.getType();
            if (!post.getImages().isEmpty()) {
                this.images = post.getImages().stream()
                        .map(Image::getPath)
                        .collect(Collectors.toList());
            }
        }
    }

    //GeneralPostSaveRequest
    @Getter
    @NoArgsConstructor
    public static class GeneralPostSaveRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;

        @Builder
        public GeneralPostSaveRequest(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public Post toEntity(Member member, PostType postType) {
            return Post.builder()
                    .writer(member)
                    .title(this.title)
                    .content(this.content)
                    .type(postType)
                    .images(new ArrayList<Image>())
                    .comments(new ArrayList<Comment>())
                    .build();
        }
    }

    //GeneralPostUpdateRequest
    @Getter
    @NoArgsConstructor
    public static class GeneralPostUpdateRequest {
        private String title;
        private String content;

        @Builder
        public GeneralPostUpdateRequest(String title, String content, List<Image> images) {
            this.title = title;
            this.content = content;
        }
    }

}
