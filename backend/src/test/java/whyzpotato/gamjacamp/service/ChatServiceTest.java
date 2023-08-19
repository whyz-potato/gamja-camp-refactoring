package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PrivateChatResponse;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @PersistenceContext
    private EntityManager em;

    private Member host;
    private Member receiver;
    private Member outsider;

    @BeforeEach
    void setUp() {
        host = new Member("a", "host", "p", Role.CUSTOMER);
        receiver = new Member("a", "receiver", "p", Role.CUSTOMER);
        outsider = new Member("a", "outsider", "p", Role.CUSTOMER);
        em.persist(host);
        em.persist(receiver);
        em.persist(outsider);
    }

    @Test
    void createPrivateChat() {
        PrivateChatResponse dto = chatService.createPrivateChat(host.getId(), receiver.getId());

        assertThat(dto.getCapacity()).isEqualTo(2);
        assertThat(dto.getNParticipants()).isEqualTo(2);
        assertThat(dto.getTo()).isEqualTo(receiver.getId());
        assertThat(dto.getTitle()).isEqualTo(receiver.getUsername());

    }


    //TODO after post
    @Test
    void createPublicChat() {
        //인원수
        //가능인원수
        //타이틀
    }

    //TODO after post
    @Test
    void enterChat() {
    }

    @Test
    @DisplayName("채팅방이 가득찬 경우 입장할 수 없다")
    void enterChat_full_fail() {
        PrivateChatResponse chat = chatService.createPrivateChat(host.getId(), receiver.getId());

        assertThrows(IllegalStateException.class, () -> {
            chatService.enterChat(chat.getRoomId(), outsider.getId());
        });
    }

    @Test
    @DisplayName("이미 참여중인 채팅방인 경우 재입장할 수 없다")
    void enterChat_entered_fail() {
        PrivateChatResponse chat = chatService.createPrivateChat(host.getId(), receiver.getId());

        assertThrows(IllegalStateException.class, () -> {
            chatService.enterChat(chat.getRoomId(), host.getId());
        });
    }

    //TODO
    @Test
    void findMessages() {
    }

    //TODO
    @Test
    void findMessagesFromStart() {
    }
}