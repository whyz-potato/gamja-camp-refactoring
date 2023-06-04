package whyzpotato.gamjacamp.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.BaseTimeEntity;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.dto.Post.GeneralPostUpdateRequestDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(value = EnumType.STRING)
    private PostType type;

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<Image>();

    @Builder
    public Post(Member writer, Chat chat, String title, String content, PostType type, List<Image> images) {
        this.writer = writer;
        this.chat = chat;
        this.title = title;
        this.content = content;
        this.type = type;
        this.images = images;
    }

    public Post update(GeneralPostUpdateRequestDto generalPostUpdateRequestDto) {
        this.title = generalPostUpdateRequestDto.getTitle();
        this.content = generalPostUpdateRequestDto.getContent();
        this.images = generalPostUpdateRequestDto.getImages();
        return this;
    }
}
