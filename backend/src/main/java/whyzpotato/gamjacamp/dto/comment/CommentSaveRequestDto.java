package whyzpotato.gamjacamp.dto.comment;

import lombok.Builder;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;

public class CommentSaveRequestDto {

    private String content;

    @Builder
    public CommentSaveRequestDto(String content) {
        this.content = content;
    }

    public Comment toEntity(Member writer, Post post) {
        return Comment.builder()
                .writer(writer)
                .post(post)
                .content(content)
                .deleteYn("N")
                .build();
    }

    public Comment toEntity(Member writer, Post post, Comment upperComment) {
        return Comment.builder()
                .writer(writer)
                .post(post)
                .upperComment(upperComment)
                .content(content)
                .deleteYn("N")
                .build();
    }
}
