package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatMemberServiceTest {

    @Autowired
    private ChatMemberService chatMemberService;

    @PersistenceContext
    private EntityManager em;

    private Member host;
    private Member newParticipant;
    private Member outsider;
    private Chat chat;

    @BeforeEach
    void setUp() {
        host = new Member("a", "host", "p", Role.CUSTOMER);
        newParticipant = new Member("a", "newParticipant", "p", Role.CUSTOMER);
        outsider = new Member("a", "outsider", "p", Role.CUSTOMER);
        chat = Chat.createPublicChat(host, "chat1", 2);
        em.persist(host);
        em.persist(newParticipant);
        em.persist(outsider);
        em.persist(chat);
    }

    @Test
    void updateLastReadMessage() {
    }

    @Test
    void isEnteredChat() {
        chat.enter(newParticipant);

        assertThat(chatMemberService.isEnteredChat(chat.getId(), host.getId())).isEqualTo(true);
        assertThat(chatMemberService.isEnteredChat(chat.getId(), newParticipant.getId())).isEqualTo(true);
        assertThat(chatMemberService.isEnteredChat(chat.getId(), outsider.getId())).isEqualTo(false);
    }

    @Test
    void enteredChatList() {

        em.persist(Chat.createPrivateChat(host, newParticipant));

        assertThat(chatMemberService.enteredChatList(host.getId()).size()).isEqualTo(2);
        assertThat(chatMemberService.enteredChatList(newParticipant.getId()).size()).isEqualTo(1);
        assertThat(chatMemberService.enteredChatList(outsider.getId())).isEmpty();

    }

    @Test
    void 채팅방나가기_채팅방멤버목록() {

        chat.enter(newParticipant);

        chatMemberService.removeChatMember(chat.getId(), newParticipant.getId());

        assertThat(chat.getChatMemberList().size()).isEqualTo(1);
        assertThat(chat.getChatMemberList().get(0).getMember()).isEqualTo(host);
    }

    @Test
    void 채팅방나가기_참여중인채팅방목록() {

        chat.enter(newParticipant);

        chatMemberService.removeChatMember(chat.getId(), newParticipant.getId());

        assertThat(chatMemberService.enteredChatList(newParticipant.getId())).isEmpty();
    }


}