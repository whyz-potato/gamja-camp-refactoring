package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MessageServiceTest {

    private static final int MAX_NUM_UNREAD = 1000;

    @Autowired
    private MessageService messageService;
    @PersistenceContext
    private EntityManager em;

    private Member host;
    private Member participant;
    private Member outsider;
    private Chat chat;


    @BeforeEach
    void setUp() {
        host = new Member("a", "host", "p", Role.CUSTOMER);
        participant = new Member("a", "newParticipant", "p", Role.CUSTOMER);
        outsider = new Member("a", "outsider", "p", Role.CUSTOMER);
        chat = Chat.createPublicChat(host, "chat1", 2);
        em.persist(host);
        em.persist(participant);
        em.persist(outsider);
        em.persist(chat);

    }

    @AfterEach
    void tearDown() {
        em.clear();
    }

    @Test
    void createMessage() {

        ChatMessageDto.DetailMessageDto dto = messageService.createMessage(chat.getId(), host.getId(), "host 입장");

        assertThat(dto.getContent()).isEqualTo("host 입장");
        assertThat(dto.getFrom().getUsername()).isSameAs("host");

    }

    @Test
    void findMessages() {

        for (int i = 0; i < 20; i++) {
            em.persist(new Message(chat, host, (i + 1) + "번 메세지"));
        }

        ChatMessageDto.MessageListDto slice = messageService.findMessages(chat.getId(), host.getId(), MAX_NUM_UNREAD);

        assertThat(slice.hasNext()).isEqualTo(true);
        assertThat(slice.getNumberOfElements()).isEqualTo(10);
        assertThat(slice.getMessages().get(0).getContent()).isEqualTo("11번 메세지");

    }

    @Test
    void findMessagesBefore() {

        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Message message = new Message(chat, host, (i + 1) + "번 메세지");
            em.persist(message);
            messages.add(message);
        }

        Message fourth = messages.get(4);
        ChatMessageDto.MessageListDto slice = messageService.findMessagesBefore(chat.getId(), host.getId(), fourth.getId());

        assertThat(slice.hasNext()).isEqualTo(false);
        assertThat(slice.getNumberOfElements()).isEqualTo(4);
        assertThat(slice.getMessages().get(0).getContent()).isEqualTo(messages.get(0).getContent());
        assertThat(slice.getMessages().get(1).getContent()).isEqualTo(messages.get(1).getContent());

    }

    @Test
    void countUnreadMessageTest() {

        for (int i = 0; i < 10; i++) {
            messageService.createMessage(chat.getId(), host.getId(), (i + 1) + "번 메세지");
        }
        em.flush();

        chat.enter(participant);
        em.flush();
        for (int i = 0; i < 20; i++) {
            messageService.createMessage(chat.getId(), host.getId(), (i + 11) + "번 메세지");
        }

        assertThat(messageService.countUnreadMessages(chat.getId(), host.getId())).isEqualTo(0);
        assertThat(messageService.countUnreadMessages(chat.getId(), participant.getId())).isEqualTo(20);
    }

    @Test
    void findMessages_haveNotRead() {

        chat.enter(participant);

        for (int i = 0; i < 20; i++) {
            em.persist(new Message(chat, host, (i + 1) + "번 메세지"));
        }

        ChatMessageDto.MessageListDto slice = messageService.findMessages(chat.getId(), participant.getId(), 15);

        assertThat(slice.hasNext()).isEqualTo(true);
        assertThat(slice.getNumberOfElements()).isEqualTo(10);
        assertThat(slice.getMessages().get(0).getContent()).isEqualTo("11번 메세지");

    }

    @Test
    void findMessages_fromUnread() {

        chat.enter(participant);
        em.flush();

        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Message message = new Message(chat, host, (i + 1) + "번 메세지");
            em.persist(message);
            messages.add(message);
        }
        ChatMember chatMember = chat.getChatMemberList().get(1);
        chatMember.updateLastReadMessage(messages.get(0));

        ChatMessageDto.MessageListDto slice = messageService.findMessages(chat.getId(), participant.getId(), 15);

        assertThat(slice.hasNext()).isEqualTo(true);
        assertThat(slice.getNumberOfElements()).isEqualTo(10);
        assertThat(slice.getMessages().get(0).getContent()).isEqualTo("1번 메세지");

    }

}