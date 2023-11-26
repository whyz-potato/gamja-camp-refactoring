package whyzpotato.gamjacamp.service;

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
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostDetail;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSaveRequest;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSimple;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostUpdateRequest;
import whyzpotato.gamjacamp.repository.ImageRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.ArrayList;
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
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        GeneralPostSaveRequest request = GeneralPostSaveRequest.builder().title("제목").content("내용").build();
        List<String> fileNameList = Stream.of("test.jpg", "test1.jpg", "test2.jpg").collect(toList());

        Long postId = postService.saveGeneralPost(member.getId(), request, fileNameList);

        Post post = postRepository.findById(postId).get();
        assertThat(post.getWriter().getId()).isEqualTo(member.getId());
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getType()).isEqualTo(PostType.GENERAL);
        assertThat(post.getImages()).hasSize(3);
        List<String> savedFileNameList = new ArrayList<String>();
        for(Image image : post.getImages()) {
            savedFileNameList.add(image.getFileName());
        }
        assertThat(savedFileNameList).contains("test.jpg", "test1.jpg", "test2.jpg");
    }

    @Test
    void 자유게시글_작성_이미지없음() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        GeneralPostSaveRequest request = GeneralPostSaveRequest.builder().title("제목").content("내용").build();

        Long postId = postService.saveGeneralPost(member.getId(), request, null);

        Post post = postRepository.findById(postId).get();
        assertThat(post.getWriter().getId()).isEqualTo(member.getId());
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getType()).isEqualTo(PostType.GENERAL);
        assertThat(post.getImages()).isEmpty();
    }

    @Test
    void 자유게시글_조회() {
        Image image = imageRepository.save(new Image("https://placeimg.com/400/500/animals", "test.jpg"));
        Image image1 = imageRepository.save(new Image("https://placeimg.com/400/600/animals", "test1.jpg"));
        Image image2 = imageRepository.save(new Image("https://placeimg.com/400/700/animals", "test2.jpg"));
        List<Image> images = Stream.of(image, image1, image2).collect(toList());
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("제목").content("내용").type(PostType.GENERAL).images(images).comments(new ArrayList<Comment>()).build());

        GeneralPostDetail generalPostDto = postService.findGeneralPost(post.getId());

        assertThat(generalPostDto.getId()).isEqualTo(post.getId());
        //assertThat(generalPostDto.getWriter()).isEqualTo(member.getId());
        assertThat(generalPostDto.getTitle()).isEqualTo("제목");
        assertThat(generalPostDto.getContent()).isEqualTo("내용");
        assertThat(generalPostDto.getPostType()).isEqualTo(PostType.GENERAL);
        assertThat(generalPostDto.getImages()).hasSize(3).contains("https://placeimg.com/400/500/animals", "https://placeimg.com/400/600/animals", "https://placeimg.com/400/700/animals");
    }

    @Test
    void 자유게시판목록_없음() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<GeneralPostSimple> posts = postService.findGeneralPostList(100L);
        assertThat(posts.getContent()).isEmpty();
    }

    @Test
    void 자유게시판목록() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Post post1 = postRepository.save(Post.builder().writer(member).title("title1").content("content1").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post2 = postRepository.save(Post.builder().writer(member).title("title2").content("content2").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post3 = postRepository.save(Post.builder().writer(member).title("title3").content("content3").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post4 = postRepository.save(Post.builder().writer(member).title("title4").content("content4").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post5 = postRepository.save(Post.builder().writer(member).title("title5").content("content5").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post6 = postRepository.save(Post.builder().writer(member).title("title6").content("content6").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post7 = postRepository.save(Post.builder().writer(member).title("title7").content("content7").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post8 = postRepository.save(Post.builder().writer(member).title("title8").content("content8").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post9 = postRepository.save(Post.builder().writer(member).title("title9").content("content9").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post10 = postRepository.save(Post.builder().writer(member).title("title10").content("content10").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post11 = postRepository.save(Post.builder().writer(member).title("title11").content("content11").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post12 = postRepository.save(Post.builder().writer(member).title("title12").content("content12").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post13 = postRepository.save(Post.builder().writer(member).title("title13").content("content13").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post14 = postRepository.save(Post.builder().writer(member).title("title14").content("content14").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post15 = postRepository.save(Post.builder().writer(member).title("title15").content("content15").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post16 = postRepository.save(Post.builder().writer(member).title("title16").content("content16").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post17 = postRepository.save(Post.builder().writer(member).title("title17").content("content17").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post18 = postRepository.save(Post.builder().writer(member).title("title18").content("content18").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post19 = postRepository.save(Post.builder().writer(member).title("title19").content("content19").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post20 = postRepository.save(Post.builder().writer(member).title("title20").content("content20").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post21 = postRepository.save(Post.builder().writer(member).title("title21").content("content21").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<GeneralPostSimple> postPage = postService.findGeneralPostList(100L);
        Page<GeneralPostSimple> postPage1 = postService.findGeneralPostList(post12.getId());
        Page<GeneralPostSimple> postPage2 = postService.findGeneralPostList(post2.getId());

        List<GeneralPostSimple> posts = postPage.getContent();
        List<GeneralPostSimple> posts1 = postPage1.getContent();
        List<GeneralPostSimple> posts2 = postPage2.getContent();
        long totalElements = postPage.getTotalElements();

        //then
        assertThat(totalElements).isEqualTo(21);

        assertThat(posts.size()).isEqualTo(10);
        assertThat(postPage.getNumber()).isEqualTo(0);
        assertThat(postPage.getTotalPages()).isEqualTo(3);
        assertThat(postPage.isFirst()).isTrue();
        assertThat(postPage.hasNext()).isTrue();

        assertThat(posts1.size()).isEqualTo(10);
        assertThat(postPage1.getNumber()).isEqualTo(0);
        assertThat(postPage1.getTotalPages()).isEqualTo(2);
        assertThat(postPage1.isFirst()).isTrue();
        assertThat(postPage1.hasNext()).isTrue();

        assertThat(posts2.size()).isEqualTo(1);
        assertThat(postPage2.getNumber()).isEqualTo(0);
        assertThat(postPage2.getTotalPages()).isEqualTo(1);
        assertThat(postPage2.isFirst()).isTrue();
        assertThat(postPage2.hasNext()).isFalse();

    }

//    @Test
//    void 자유게시판목록_이미지있음() {
//    }
//
//    @Test
//    void 자유게시판목록_이미지없음() {
//    }

    @Test
    void 자유게시글_수정() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Image image = imageRepository.save(new Image("https://placeimg.com/400/500/animals", "test.jpg"));
        Image image1 = imageRepository.save(new Image("https://placeimg.com/400/600/animals", "test1.jpg"));
        Image image2 = imageRepository.save(new Image("https://placeimg.com/400/700/animals", "test2.jpg"));
        List<Image> images = Stream.of(image, image1, image2).collect(toList());
        Post post = postRepository.save(Post.builder().writer(member).title("제목").content("내용").type(PostType.GENERAL).images(images).build());

        List<String> updateImagesFileName = Stream.of("test.jpg", "test4.jpg").collect(toList());
        GeneralPostUpdateRequest request = GeneralPostUpdateRequest.builder().title("수정된제목").content("수정된내용").build();

        imageRepository.deleteAll();
        GeneralPostDetail generalPostDetail = postService.updateGeneralPost(member.getId(), post.getId(), request, updateImagesFileName);

        assertThat(generalPostDetail.getId()).isEqualTo(post.getId());
//        assertThat(generalPostDetail.getWriter()).isEqualTo(member.getId());
        assertThat(generalPostDetail.getTitle()).isEqualTo("수정된제목");
        assertThat(generalPostDetail.getContent()).isEqualTo("수정된내용");
        assertThat(generalPostDetail.getImages()).hasSize(2);
        assertThat(generalPostDetail.getImages()).contains("https://gamja-camp.s3.ap-northeast-2.amazonaws.com/test.jpg", "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/test4.jpg");
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
        postRepository.save(Post.builder().writer(member).title("key").content("content1").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title2").content("content2").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title3").content("key").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title4").content("content4").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Post post = postRepository.save(Post.builder().writer(member).title("key").content("content5").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title6").content("content6").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title7").content("key").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title8").content("content8").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content9").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title10").content("content10").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title11").content("key").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title12").content("content12").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content13").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title14").content("content14").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title15").content("key").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title16").content("content16").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content17").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title18").content("content18").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title19").content("key").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title20").content("content20").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("key").content("content21").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title22").content("content22").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        postRepository.save(Post.builder().writer(member).title("title23").content("key").type(PostType.GENERAL).images(new ArrayList<>()).comments(new ArrayList<>()).build());
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<GeneralPostSimple> postPage = postService.search(100L, "key");
        Page<GeneralPostSimple> postPage1 = postService.search(post.getId(), "key");

        List<GeneralPostSimple> posts = postPage.getContent();
        List<GeneralPostSimple> posts1 = postPage1.getContent();
        long totalElements = postPage.getTotalElements();

        //then
        assertThat(totalElements).isEqualTo(12);

        assertThat(posts.size()).isEqualTo(10);
        assertThat(postPage.getNumber()).isEqualTo(0);
        assertThat(postPage.getTotalPages()).isEqualTo(2);
        assertThat(postPage.isFirst()).isTrue();
        assertThat(postPage.hasNext()).isTrue();

        assertThat(posts1.size()).isEqualTo(2);
        assertThat(postPage1.getNumber()).isEqualTo(0);
        assertThat(postPage1.getTotalPages()).isEqualTo(1);
        assertThat(postPage1.isFirst()).isTrue();
        assertThat(postPage1.hasNext()).isFalse();
    }
}
