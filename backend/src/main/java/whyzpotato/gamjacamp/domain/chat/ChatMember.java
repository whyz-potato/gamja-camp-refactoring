package whyzpotato.gamjacamp.domain.chat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.BaseTimeEntity;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatMember extends BaseTimeEntity {

    // 복합키 대신 대리키 사용
    @Id
    @GeneratedValue
    @Column(name = "chat_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = true)
    private Member receiver = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_read_message_id")
    private Message lastReadMessage = null;

    @NotNull
    private String title;

    public ChatMember(Chat chat, Member member, String title) {
        this.chat = chat;
        this.member = member;
        this.title = title;
    }

    public ChatMember(Chat chat, Member sender, Member receiver) {
        this.chat = chat;
        this.member = sender;
        this.receiver = receiver;
        this.title = receiver.getUsername();
    }

    public void updateLastReadMessage(Message lastReadMessage) {
        this.lastReadMessage = lastReadMessage;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return this.member.getUsername();
    }


}
