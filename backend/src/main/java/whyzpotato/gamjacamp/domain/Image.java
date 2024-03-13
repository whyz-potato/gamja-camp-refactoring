package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.post.Post;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false, unique = true)
    private String path;

    @Column(nullable = false)
    private String fileName;

    @Builder
    public Image(Camp camp, Room room, Review review, Post post, String path, String fileName) {
        this.camp = camp;
        this.room = room;
        this.review = review;
        this.post = post;
        this.path = path;
        this.fileName = fileName;
    }

    public Image(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }
}
