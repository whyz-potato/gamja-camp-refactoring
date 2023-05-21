package whyzpotato.gamjacamp.domain.chat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chat {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member host;

    @OneToMany(mappedBy = "chat")
    private List<ChatMember> chatMemberList = new ArrayList<ChatMember>();

    @Column(length = 20)
    private String title;

    private int capacity;

    private String lastMessage;

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

}
