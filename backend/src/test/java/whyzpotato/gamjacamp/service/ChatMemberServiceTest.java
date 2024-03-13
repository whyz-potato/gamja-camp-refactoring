package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.repository.ChatMemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatMemberServiceTest {

    @Autowired
    private ChatMemberService chatMemberService;

    @Autowired
    private ChatMemberRepository chatMemberRepository;

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

    @AfterEach
    void tearDown() {
        em.clear();
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

    @RepeatedTest(5)
    void 안읽은메세지_입장후카운트() throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            em.persist(new Message(chat, host, "message"));
        }
        em.flush();

        chat.enter(newParticipant);
        chatMemberRepository.findByChatAndMember(chat, newParticipant).get().getCreatedTime();
        for (int i = 0; i < 5; i++) {
            em.persist(new Message(chat, host, "message"));
        }

        assertThat(chatMemberService.countUnreadMessages(newParticipant.getId())).isEqualTo(5);

    }


    @RepeatedTest(5)
    void 안읽은메세지_lastReadMessage이용() {

        chat.enter(newParticipant);
        for (int i = 0; i < 3; i++) {
            Message message = new Message(chat, host, "message");
            em.persist(message);
            chatMemberService.updateLastReadMessage(chat.getId(), newParticipant.getId(), message.getId());
        }
        em.flush();

        for (int i = 0; i < 5; i++) {
            em.persist(new Message(chat, host, "message"));
        }

        assertThat(chatMemberService.countUnreadMessages(newParticipant.getId())).isEqualTo(5);

    }


    @RepeatedTest(5)
    void 안읽은메세지_채팅방여러개() {

        Chat chat1 = Chat.createPrivateChat(host, newParticipant);
        Chat chat2 = Chat.createPublicChat(host, "chat1", 2);
        em.persist(chat1);
        em.persist(chat2);

        for (int i = 0; i < 5; i++) {
            Message message = new Message(chat1, host, "message");
            em.persist(message);
            chatMemberService.updateLastReadMessage(chat1.getId(), newParticipant.getId(), message.getId());
        }
        for (int i = 0; i < 10; i++) {
            em.persist(new Message(chat1, host, "message"));
        }
        for (int i = 0; i < 10; i++) {
            em.persist(new Message(chat2, host, "message"));
        }

        chat2.enter(newParticipant);
        em.flush();

        chatMemberRepository.findByChatAndMember(chat2, newParticipant).get().getCreatedTime();
        for (int i = 0; i < 7; i++) {
            em.persist(new Message(chat2, host, "message"));
        }


        assertThat(chatMemberService.countUnreadMessages(newParticipant.getId())).isEqualTo(17);

    }

}