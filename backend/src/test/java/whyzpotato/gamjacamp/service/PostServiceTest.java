package whyzpotato.gamjacamp.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;
import whyzpotato.gamjacamp.dto.Post.SimpleGeneralPostResponseDto;
import whyzpotato.gamjacamp.repository.ImageRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class PostServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PostService postService;

    @Test
    void 자유게시판목록_이미지테스트() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Image image = new Image("https://placeimg.com/400/500/animals", "test.jpg");
        Image image1 = new Image("https://placeimg.com/400/600/animals", "test1.jpg");
        Image image2 = new Image("https://placeimg.com/400/700/animals", "test2.jpg");
        imageRepository.save(image);
        imageRepository.save(image1);
        imageRepository.save(image2);

        List<Image> images = new ArrayList<>();
        images.add(image);
        images.add(image1);
        images.add(image2);

        Post post = postRepository.save(Post.builder()
                .writer(member)
                .title("title")
                .content("content")
                .type(PostType.GENERAL)
                .images(images).build());

        SimpleGeneralPostResponseDto dto = new SimpleGeneralPostResponseDto(post);
        System.out.println("dto = " + dto);
    }

    @Test
    void 자유게시판목록_이미지없음() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Post post = postRepository.save(Post.builder()
                .writer(member)
                .title("title")
                .content("content")
                .type(PostType.GENERAL)
                .build());

        SimpleGeneralPostResponseDto dto = new SimpleGeneralPostResponseDto(post);
        System.out.println("dto = " + dto);
    }

    @Test
    void 자유게시판목록() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        postRepository.save(Post.builder().writer(member).title("title1").content("content1").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title2").content("content2").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title3").content("content3").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title4").content("content4").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title5").content("content5").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title6").content("content6").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title7").content("content7").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title8").content("content8").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title9").content("content9").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title10").content("content10").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title11").content("content11").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title12").content("content12").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title13").content("content13").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title14").content("content14").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title15").content("content15").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title16").content("content16").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title17").content("content17").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title18").content("content18").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title19").content("content19").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title20").content("content20").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title21").content("content21").type(PostType.GENERAL).build());
        Pageable pageable = PageRequest.of(0, 10);
        Page<SimpleGeneralPostResponseDto> posts = postService.findGeneralPostList(23L, pageable);
        System.out.println("Page1");
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("Id: Title = " + post.getId() + ": " + post.getTitle());
        }
        posts = postService.findGeneralPostList(13L, pageable);
        System.out.println("Page2");
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("Id: Title = " + post.getId() + ": " + post.getTitle());
        }
        posts = postService.findGeneralPostList(3L, pageable);
        System.out.println("Page3");
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("Id: Title = " + post.getId() + ": " + post.getTitle());
        }
    }

    @Test
    void 자유게시판목록_없음() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SimpleGeneralPostResponseDto> posts = postService.findGeneralPostList(23L, pageable);
        assertThat(posts.getContent()).isEmpty();
    }

    @Test
    void 자유게시판_검색() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content1").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title2").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content3").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title4").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content5").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title6").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content7").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title8").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content9").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title10").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content11").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title12").content("key").type(PostType.GENERAL).build());

        Pageable pageable = PageRequest.of(0, 10);
        Page<SimpleGeneralPostResponseDto> posts = postService.search(14L, "key", pageable);
        System.out.println("Page1");
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("Id/Title/Content = " + post.getId() + "/" + post.getTitle() + "/" + post.getContent());
        }
        posts = postService.search(4L, "key", pageable);
        System.out.println("Page2");
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("Id/Title/Content = " + post.getId() + "/" + post.getTitle() + "/" + post.getContent());
        }
    }

}
