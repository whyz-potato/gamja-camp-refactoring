package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.dto.comment.CommentDto.CommentDetail;
import whyzpotato.gamjacamp.dto.comment.CommentDto.CommentInfo;
import whyzpotato.gamjacamp.dto.comment.CommentDto.CommentSaveRequest;
import whyzpotato.gamjacamp.dto.comment.CommentDto.CommentUpdateRequest;
import whyzpotato.gamjacamp.repository.CommentRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long save(Long memberId, Long postId, CommentSaveRequest request) {
        Member writer = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        Comment comment = request.toEntity(writer, post);
        if(request.getUpperCommentId() != null) {
            Comment upperComment = commentRepository.findById(request.getUpperCommentId()).get();
            comment.updateUpperComment(upperComment);
            upperComment.getLowerComments().add(comment);
        }
        post.getComments().add(comment);
        return commentRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public List<CommentInfo> findCommentList(Long postId) {
        List<Comment> comments = commentRepository.findAllByPost(postRepository.findById(postId).get()).get();  //post.getComments()
        List<CommentInfo> commentList = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getUpperComment() != null)
                continue;
            commentList.add(new CommentInfo(comment));
        }
        return commentList;
    }

    public CommentDetail update(Long memberId, Long commentId, CommentUpdateRequest request) {
        Member writer = memberRepository.findById(memberId).get();
        Comment comment = commentRepository.findById(commentId).get();
        if(comment.getWriter().getId().equals(writer.getId())) {
            commentRepository.save(comment.update(request));
            return new CommentDetail(comment);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void delete(Long memberId, Long commentId) {
        Member writer = memberRepository.findById(memberId).get();
        Comment comment = commentRepository.findById(commentId).get();
        Post post = postRepository.findById(comment.getPost().getId()).get();
        if(comment.getWriter().getId().equals(writer.getId())) {
            if(comment.getUpperComment() != null) { // 대댓글인 경우
                comment.getUpperComment().getLowerComments().remove(comment);
            } else if (comment.getUpperComment() == null && !comment.getLowerComments().isEmpty()) { // 댓글이고, 대댓글이 존재하는 경우
                // Post의 comments에서 대댓글 삭제
                post.getComments().removeAll(comment.getLowerComments());
                commentRepository.deleteAll(comment.getLowerComments());
            }
            post.getComments().remove(comment); // Post의 comments에서 댓글 삭제
            commentRepository.delete(comment);
            return;
        }
        throw new NoSuchElementException();
    }
}
