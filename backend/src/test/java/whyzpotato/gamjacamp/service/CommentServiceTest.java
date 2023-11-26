package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentSaveRequest;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentUpdateRequest;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;
import whyzpotato.gamjacamp.repository.CommentRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.ArrayList;

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
        CommentSaveRequest request = CommentSaveRequest.builder().content("첫번째댓글").upperComment(null).build();

        Long commentId = commentService.save(member.getId(), post.getId(), request);

        Comment comment = commentRepository.findById(commentId).get();
        assertThat(comment.getContent()).isEqualTo("첫번째댓글");
        assertThat(comment.getWriter().getId()).isEqualTo(member.getId());
        assertThat(comment.getPost().getId()).isEqualTo(post.getId());
        assertThat(comment.getUpperComment()).isNull();
        assertThat(comment.getLowerComments()).isEmpty();
    }

    @Test
    void 대댓글_작성() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("게시글제목").content("게시글내용").type(PostType.GENERAL).build());
        Comment upperComment = commentRepository.save(Comment.builder().writer(member).post(post).content("첫번째댓글").lowerComments(new ArrayList<Comment>()).build());
        CommentSaveRequest request = CommentSaveRequest.builder().content("첫번째대댓글").upperComment(upperComment.getId()).build();

        Long commentId = commentService.save(member.getId(), post.getId(), request);

        Comment reComment = commentRepository.findById(commentId).get();
        assertThat(reComment.getContent()).isEqualTo("첫번째대댓글");
        assertThat(reComment.getWriter().getId()).isEqualTo(member.getId());
        assertThat(reComment.getPost().getId()).isEqualTo(post.getId());
        assertThat(reComment.getUpperComment().getId()).isEqualTo(upperComment.getId());
        assertThat(commentRepository.findById(reComment.getUpperComment().getId()).get().getLowerComments()).hasSize(1);
        assertThat(reComment.getLowerComments()).isEmpty();
    }

    @Test
    void 댓글_수정() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_CUSTOMER).build());
        Post post = postRepository.save(Post.builder().writer(member).title("게시글제목").content("게시글내용").type(PostType.GENERAL).build());
        Comment comment = commentRepository.save(Comment.builder().writer(member).post(post).content("첫번째댓글").build());
        CommentUpdateRequest request = CommentUpdateRequest.builder().content("수정된내용").build();

        commentService.update(member.getId(), comment.getId(), request);

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
