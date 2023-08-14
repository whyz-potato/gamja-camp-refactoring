package whyzpotato.gamjacamp.domain.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatTest {

    private Member member1, member2, member3;

    @BeforeEach
    void init() {
        member1 = Member.builder().account("account1@mail.com").role(Role.CUSTOMER).username("member1").picture("img").build();
        member2 = Member.builder().account("account2@mail.com").role(Role.CUSTOMER).username("member2").picture("img").build();
        member3 = Member.builder().account("account3@mail.com").role(Role.CUSTOMER).username("member3").picture("img").build();
    }

    @Test
    @DisplayName("단체 채팅 개설 성공")
    void createPublicChat() {
        Chat chat = Chat.createPublicChat(member1, "모두 모여라 test", 3);

        assertThat(chat.getHost()).isEqualTo(member1);
        assertThat(chat.getCapacity()).isEqualTo(3);
        assertThat(chat.getTitle()).isEqualTo("모두 모여라 test");
    }

    @Test
    @DisplayName("단체 채팅 방장 조회 성공")
    void publicChat_host() {
        Chat chat = Chat.createPublicChat(member1, "모두 모여라 test", 3);
        assertThat(chat.getHost()).isEqualTo(member1);
    }

    @Test
    @DisplayName("단체 채팅 멤버 조회 성공")
    void publicChat_member() {
        Chat chat = Chat.createPublicChat(member1, "모두 모여라 test", 3);

        List<ChatMember> members = chat.getChatMemberList();

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getMember()).isEqualTo(member1);
    }

    @Test
    @DisplayName("개인 채팅 개설 성공")
    void createPrivateChat() {
        Chat privateChat = Chat.createPrivateChat(member1, member2);
        assertThat(privateChat.getHost()).isEqualTo(member1);
    }

    @Test
    @DisplayName("개인 채팅 채팅인원")
    void privateChat_members() {
        Chat privateChat = Chat.createPrivateChat(member1, member2);

        List<ChatMember> members = privateChat.getChatMemberList();

        assertThat(members.get(0).getMember()).isEqualTo(member1);
        assertThat(members.get(1).getMember()).isEqualTo(member2);
    }

    @Test
    @DisplayName("개인 채팅 제목의 초기값은 채팅 상대의 이름이다")
    void privateChat_title() {
        Chat privateChat = Chat.createPrivateChat(member1, member2);

        List<ChatMember> members = privateChat.getChatMemberList();

        assertThat(members.get(0).getTitle()).isEqualTo(member2.getUsername());
        assertThat(members.get(1).getTitle()).isEqualTo(member1.getUsername());
    }

}