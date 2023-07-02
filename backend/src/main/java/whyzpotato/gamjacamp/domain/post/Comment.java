package whyzpotato.gamjacamp.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.BaseTimeEntity;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.dto.comment.CommentUpdateRequestDto;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_comment_id")
    private Comment upperComment;

    @Column(length = 300)
    private String content;

    @Column(name = "delete_yn")
    private String deleteYn;

    // TODO : 삭제 이후에는 admin만 댓글 확인 할 수 있게 인가

    @Builder
    public Comment(Post post, Member writer, Comment upperComment, String content, String deleteYn) {
        this.post = post;
        this.writer = writer;
        this.upperComment = upperComment;
        this.content = content;
        this.deleteYn = deleteYn;
    }

    public Comment update(CommentUpdateRequestDto commentUpdateRequestDto) {
        this.content = commentUpdateRequestDto.getContent();
        return this;
    }

    public Comment delete() {
        this.deleteYn = "Y";
        return this;
    }
}
