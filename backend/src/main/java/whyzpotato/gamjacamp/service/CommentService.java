package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.dto.comment.CommentSaveRequestDto;
import whyzpotato.gamjacamp.dto.comment.CommentUpdateRequestDto;
import whyzpotato.gamjacamp.repository.CommentRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long save(Long memberId, Long postId, CommentSaveRequestDto commentSaveRequestDto) {
        Member writer = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        return commentRepository.save(commentSaveRequestDto.toEntity(writer,post)).getId();
    }

    public Long saveReComment(Long memberId, Long postId, Long upperCommentId, CommentSaveRequestDto commentSaveRequestDto) {
        Member writer = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        Comment upperComment = commentRepository.findById(upperCommentId).get();
        return commentRepository.save(commentSaveRequestDto.toEntity(writer, post, upperComment)).getId();
    }

    public void findCommentList() {

    }

    //TODO dto
    public void update(Long memberId, Long commentId, CommentUpdateRequestDto commentUpdateRequestDto) {
        Member writer = memberRepository.findById(memberId).get();
        Comment comment = commentRepository.findById(commentId).get();
        if(comment.getWriter().getId().equals(writer.getId())) {
            commentRepository.save(comment.update(commentUpdateRequestDto));
            return;
        }
        throw new NoSuchElementException();
    }

    public void delete(Long memberId, Long commentId) {
        Member writer = memberRepository.findById(memberId).get();
        Comment comment = commentRepository.findById(commentId).get();
        if(comment.getWriter().getId().equals(writer.getId())) {
            commentRepository.save(comment.delete());
            return;
        }
        throw new NoSuchElementException();
    }
}
