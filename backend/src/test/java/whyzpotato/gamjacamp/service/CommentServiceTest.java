package whyzpotato.gamjacamp.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;
import whyzpotato.gamjacamp.dto.comment.CommentSaveRequestDto;
import whyzpotato.gamjacamp.dto.comment.CommentUpdateRequestDto;
import whyzpotato.gamjacamp.repository.CommentRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @Test
    void 댓글_작성() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("게시글제목").content("게시글내용").type(PostType.GENERAL).build());
        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder().content("첫번째댓글").build();

        Long commentId = commentService.save(member.getId(), post.getId(), commentSaveRequestDto);

        Comment comment = commentRepository.findById(commentId).get();
        assertThat(comment.getContent()).isEqualTo("첫번째댓글");
        assertThat(comment.getWriter().getId()).isEqualTo(member.getId());
        assertThat(comment.getPost().getId()).isEqualTo(post.getId());
    }

    @Test
    void 대댓글_작성() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("게시글제목").content("게시글내용").type(PostType.GENERAL).build());
        Comment comment = commentRepository.save(Comment.builder().writer(member).post(post).content("첫번째댓글").build());
        CommentSaveRequestDto commentSaveRequestDto = CommentSaveRequestDto.builder().content("첫번째대댓글").build();

        Long commentId = commentService.saveReComment(member.getId(), post.getId(), comment.getId(), commentSaveRequestDto);

        Comment reComment = commentRepository.findById(commentId).get();
        assertThat(reComment.getContent()).isEqualTo("첫번째대댓글");
        assertThat(reComment.getWriter().getId()).isEqualTo(member.getId());
        assertThat(reComment.getPost().getId()).isEqualTo(post.getId());
        assertThat(reComment.getUpperComment().getId()).isEqualTo(comment.getId());
    }

    @Test
    void 댓글_수정() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("게시글제목").content("게시글내용").type(PostType.GENERAL).build());
        Comment comment = commentRepository.save(Comment.builder().writer(member).post(post).content("첫번째댓글").build());
        CommentUpdateRequestDto commentUpdateRequestDto = CommentUpdateRequestDto.builder().content("수정된내용").build();

        commentService.update(member.getId(), comment.getId(), commentUpdateRequestDto);

        Comment updateComment = commentRepository.findById(comment.getId()).get();
        assertThat(updateComment.getContent()).isEqualTo("수정된내용");
    }

    @Test
    void 댓글_삭제() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("게시글제목").content("게시글내용").type(PostType.GENERAL).build());
        Comment comment = commentRepository.save(Comment.builder().writer(member).post(post).content("첫번째댓글").build());

        commentService.delete(member.getId(), comment.getId());

        Comment deleteComment = commentRepository.findById(comment.getId()).get();
        assertThat(deleteComment.getDeleteYn()).isEqualTo("Y");
    }
}
