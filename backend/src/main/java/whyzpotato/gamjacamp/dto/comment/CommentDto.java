package whyzpotato.gamjacamp.dto.comment;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CommentDto {

    @Getter
    @NoArgsConstructor
    public static class CommentSimple {
        private Long id;
        private String content;

        public CommentSimple(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentInfo {
        private Long id;
        private String writer;
        private String content;
        private List<CommentInfo> lowerComments;

        @Builder
        public CommentInfo(Long id, String writer, String content, List<CommentInfo> lowerComments) {
            this.id = id;
            this.writer = writer;
            this.content = content;
            this.lowerComments = lowerComments;
        }

        public CommentInfo(Comment comment) {
            this.id = comment.getId();
            this.writer = comment.getWriter().getUsername();
            this.content = comment.getContent();
            if(!comment.getLowerComments().isEmpty()) {
                this.lowerComments = comment.getLowerComments().stream()
                        .map(CommentInfo::new)
                        .collect(Collectors.toList());
            }
        }
    }


    @Getter
    @NoArgsConstructor
    public static class CommentDetail {
        private Long id;
        private Long post;
        private Long writer;
        private Long upperComment;
        private List<CommentDetail> lowerComments;
        private String content;
        private String deleteYn;

        @Builder
        public CommentDetail(Long id, Long postId, Long memberId, Long upperCommentId, List<CommentDetail> lowerComments, String content, String deleteYn) {
            this.id = id;
            this.post = postId;
            this.writer = memberId;
            this.upperComment = upperCommentId;
            this.lowerComments = lowerComments;
            this.content = content;
            this.deleteYn = deleteYn;
        }

        public CommentDetail(Comment comment) {
            this.id = comment.getId();
            this.post = comment.getPost().getId();
            this.writer = comment.getWriter().getId();
            if(comment.getUpperComment() != null) {
                this.upperComment = comment.getUpperComment().getId();
            }
            if(!comment.getLowerComments().isEmpty()) {
                this.lowerComments = comment.getLowerComments().stream()
                        .map(CommentDetail::new)
                        .collect(Collectors.toList());
            }
            this.content = comment.getContent();
            this.deleteYn = comment.getDeleteYn();
        }

        public List<CommentDetail> toList(List<Comment> comments) {
            List<CommentDetail> commentDetailList = new ArrayList<>();
            for (Comment comment : comments) {
                commentDetailList.add(new CommentDetail(comment));
            }
            return commentDetailList;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentSaveRequest {
        private String content;
        private Long upperCommentId;

        @Builder
        public CommentSaveRequest(String content, Long upperComment) {
            this.content = content;
            this.upperCommentId = upperComment;
        }

        public Comment toEntity(Member writer, Post post) {
            return Comment.builder()
                    .writer(writer)
                    .post(post)
                    .content(content)
                    .lowerComments(new ArrayList<Comment>())
                    .deleteYn("N")
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentUpdateRequest {
        private String content;

        @Builder
        public CommentUpdateRequest(String content) {
            this.content = content;
        }

    }
}
