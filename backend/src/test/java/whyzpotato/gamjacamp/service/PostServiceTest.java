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
import whyzpotato.gamjacamp.dto.Post.GeneralPostDto;
import whyzpotato.gamjacamp.dto.Post.GeneralPostSaveRequestDto;
import whyzpotato.gamjacamp.dto.Post.GeneralPostUpdateRequestDto;
import whyzpotato.gamjacamp.dto.Post.SimpleGeneralPostResponseDto;
import whyzpotato.gamjacamp.repository.ImageRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
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
    void 자유게시글_작성() {
        Image image = imageRepository.save(new Image("https://placeimg.com/400/500/animals", "test.jpg"));
        Image image1 = imageRepository.save(new Image("https://placeimg.com/400/600/animals", "test1.jpg"));
        Image image2 = imageRepository.save(new Image("https://placeimg.com/400/700/animals", "test2.jpg"));
        List<Image> images = Stream.of(image, image1, image2).collect(toList());

        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        GeneralPostSaveRequestDto generalPostSaveRequestDto = GeneralPostSaveRequestDto.builder().title("제목").content("내용").images(images).build();

        Long postId = postService.saveGeneralPost(member.getId(), generalPostSaveRequestDto);

        Post post = postRepository.findById(postId).get();
        assertThat(post.getWriter().getId()).isEqualTo(member.getId());
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getType()).isEqualTo(PostType.GENERAL);
        assertThat(post.getImages()).hasSize(3).contains(image, image1, image2);
    }

    @Test
    void 자유게시글_조회() {
        Image image = imageRepository.save(new Image("https://placeimg.com/400/500/animals", "test.jpg"));
        Image image1 = imageRepository.save(new Image("https://placeimg.com/400/600/animals", "test1.jpg"));
        Image image2 = imageRepository.save(new Image("https://placeimg.com/400/700/animals", "test2.jpg"));
        List<Image> images = Stream.of(image, image1, image2).collect(toList());
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("제목").content("내용").type(PostType.GENERAL).images(images).build());

        GeneralPostDto generalPostDto = postService.findGeneralPost(post.getId());

        assertThat(generalPostDto.getId()).isEqualTo(post.getId());
        assertThat(generalPostDto.getMemberId()).isEqualTo(member.getId());
        assertThat(generalPostDto.getTitle()).isEqualTo("제목");
        assertThat(generalPostDto.getContent()).isEqualTo("내용");
        assertThat(generalPostDto.getPostType()).isEqualTo(PostType.GENERAL);
        assertThat(generalPostDto.getImages()).hasSize(3).contains(image, image1, image2);
    }

    @Test
    void 자유게시판목록_없음() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SimpleGeneralPostResponseDto> posts = postService.findGeneralPostList(23L, pageable);
        assertThat(posts.getContent()).isEmpty();
    }

    @Test
    void 자유게시판목록() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Post post1 = postRepository.save(Post.builder().writer(member).title("title1").content("content1").type(PostType.GENERAL).build());
        Post post2 = postRepository.save(Post.builder().writer(member).title("title2").content("content2").type(PostType.GENERAL).build());
        Post post3 = postRepository.save(Post.builder().writer(member).title("title3").content("content3").type(PostType.GENERAL).build());
        Post post4 = postRepository.save(Post.builder().writer(member).title("title4").content("content4").type(PostType.GENERAL).build());
        Post post5 = postRepository.save(Post.builder().writer(member).title("title5").content("content5").type(PostType.GENERAL).build());
        Post post6 = postRepository.save(Post.builder().writer(member).title("title6").content("content6").type(PostType.GENERAL).build());
        Post post7 = postRepository.save(Post.builder().writer(member).title("title7").content("content7").type(PostType.GENERAL).build());
        Post post8 = postRepository.save(Post.builder().writer(member).title("title8").content("content8").type(PostType.GENERAL).build());
        Post post9 = postRepository.save(Post.builder().writer(member).title("title9").content("content9").type(PostType.GENERAL).build());
        Post post10 = postRepository.save(Post.builder().writer(member).title("title10").content("content10").type(PostType.GENERAL).build());
        Post post11 = postRepository.save(Post.builder().writer(member).title("title11").content("content11").type(PostType.GENERAL).build());
        Post post12 = postRepository.save(Post.builder().writer(member).title("title12").content("content12").type(PostType.GENERAL).build());
        Post post13 = postRepository.save(Post.builder().writer(member).title("title13").content("content13").type(PostType.GENERAL).build());
        Post post14 = postRepository.save(Post.builder().writer(member).title("title14").content("content14").type(PostType.GENERAL).build());
        Post post15 = postRepository.save(Post.builder().writer(member).title("title15").content("content15").type(PostType.GENERAL).build());
        Post post16 = postRepository.save(Post.builder().writer(member).title("title16").content("content16").type(PostType.GENERAL).build());
        Post post17 = postRepository.save(Post.builder().writer(member).title("title17").content("content17").type(PostType.GENERAL).build());
        Post post18 = postRepository.save(Post.builder().writer(member).title("title18").content("content18").type(PostType.GENERAL).build());
        Post post19 = postRepository.save(Post.builder().writer(member).title("title19").content("content19").type(PostType.GENERAL).build());
        Post post20 = postRepository.save(Post.builder().writer(member).title("title20").content("content20").type(PostType.GENERAL).build());
        Post post21 = postRepository.save(Post.builder().writer(member).title("title21").content("content21").type(PostType.GENERAL).build());
        Pageable pageable = PageRequest.of(0, 10);


        System.out.println("Page1");
        Page<SimpleGeneralPostResponseDto> posts = postService.findGeneralPostList(23L, pageable);
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("[" + post.getId() + "] - " + post.getTitle());
        }

        System.out.println("Page2");
        posts = postService.findGeneralPostList(13L, pageable);
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("[" + post.getId() + "] - " + post.getTitle());
        }

        System.out.println("Page3");
        posts = postService.findGeneralPostList(3L, pageable);
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("[" + post.getId() + "] - " + post.getTitle());
        }
    }

//    @Test
//    void 자유게시판목록_이미지테스트() {
//        Image image = imageRepository.save(new Image("https://placeimg.com/400/500/animals", "test.jpg"));
//        Image image1 = imageRepository.save(new Image("https://placeimg.com/400/600/animals", "test1.jpg"));
//        Image image2 = imageRepository.save(new Image("https://placeimg.com/400/700/animals", "test2.jpg"));
//        List<Image> images = Stream.of(image, image1, image2).collect(toList());
//        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
//        Post post = postRepository.save(Post.builder().writer(member).title("title").content("content").type(PostType.GENERAL).images(images).build());
//
//
//        SimpleGeneralPostResponseDto dto = new SimpleGeneralPostResponseDto(post);
//        System.out.println("dto = " + dto);
//    }
//
//    @Test
//    void 자유게시판목록_이미지없음() {
//        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
//        Post post = postRepository.save(Post.builder()
//                .writer(member)
//                .title("title")
//                .content("content")
//                .type(PostType.GENERAL)
//                .build());
//
//        SimpleGeneralPostResponseDto dto = new SimpleGeneralPostResponseDto(post);
//        System.out.println("dto = " + dto);
//    }

    @Test
    void 자유게시글_수정() {
        Image image = imageRepository.save(new Image("https://placeimg.com/400/500/animals", "test.jpg"));
        Image image1 = imageRepository.save(new Image("https://placeimg.com/400/600/animals", "test1.jpg"));
        Image image2 = imageRepository.save(new Image("https://placeimg.com/400/700/animals", "test2.jpg"));
        Image image3 = imageRepository.save(new Image("https://placeimg.com/400/800/animals", "test3.jpg"));
        Image image4 = imageRepository.save(new Image("https://placeimg.com/400/900/animals", "test4.jpg"));
        List<Image> images = Stream.of(image, image1, image2).collect(toList());
        List<Image> updateImages = Stream.of(image1, image2, image3, image4).collect(toList());
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("제목").content("내용").type(PostType.GENERAL).images(images).build());
        GeneralPostUpdateRequestDto generalPostUpdateRequestDto = GeneralPostUpdateRequestDto.builder().title("수정된제목").content("수정된내용").images(updateImages).build();

        GeneralPostDto generalPostDto = postService.updateGeneralPost(member.getId(), post.getId(), generalPostUpdateRequestDto);

        assertThat(generalPostDto.getId()).isEqualTo(post.getId());
        assertThat(generalPostDto.getMemberId()).isEqualTo(member.getId());
        assertThat(generalPostDto.getTitle()).isEqualTo("수정된제목");
        assertThat(generalPostDto.getContent()).isEqualTo("수정된내용");
        assertThat(generalPostDto.getImages()).hasSize(4).contains(image1, image2, image3, image4).doesNotContain(image);
    }

    @Test
    void 자유게시글_삭제() {
        Image image = imageRepository.save(new Image("https://placeimg.com/400/500/animals", "test.jpg"));
        Image image1 = imageRepository.save(new Image("https://placeimg.com/400/600/animals", "test1.jpg"));
        Image image2 = imageRepository.save(new Image("https://placeimg.com/400/700/animals", "test2.jpg"));
        List<Image> images = Stream.of(image, image1, image2).collect(toList());
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("제목").content("내용").type(PostType.GENERAL).images(images).build());

        postService.delete(member.getId(), post.getId());

        Optional<Post> deletePost = postRepository.findById(post.getId());
        assertThat(deletePost.isPresent()).isFalse();
    }

    @Test
    void 자유게시판_검색() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content1").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title2").content("content2").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title3").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title4").content("content4").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content5").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title6").content("content6").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title7").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title8").content("content8").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content9").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title10").content("content10").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title11").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title12").content("content12").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content13").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title14").content("content14").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title15").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title16").content("content16").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content17").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title18").content("content18").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title19").content("key").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title20").content("content20").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content21").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title22").content("content22").type(PostType.GENERAL).build());
        postRepository.save(Post.builder().writer(member).title("title23").content("key").type(PostType.GENERAL).build());
        Pageable pageable = PageRequest.of(0, 10);

        System.out.println("Page1");
        Page<SimpleGeneralPostResponseDto> posts = postService.search(25L, "key", pageable);
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("[" + post.getId() + "] - " + post.getTitle()+ " : " + post.getContent());
        }

        System.out.println("Page2");
        posts = postService.search(6L, "key", pageable);
        for (SimpleGeneralPostResponseDto post : posts.getContent()) {
            System.out.println("[" + post.getId() + "] - " + post.getTitle()+ " : " + post.getContent());
        }
    }
}
