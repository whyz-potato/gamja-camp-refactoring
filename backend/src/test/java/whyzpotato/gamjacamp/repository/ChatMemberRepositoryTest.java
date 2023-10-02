package whyzpotato.gamjacamp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatMemberRepositoryTest {

    @Autowired
    private ChatMemberRepository chatMemberRepository;

    @PersistenceContext
    private EntityManager em;

    private Member sender;
    private Member receiver;
    private Chat chat1, chat2;

    @BeforeEach
    void setUp() {
        sender = new Member("a", "sender", "p", Role.CUSTOMER);
        receiver = new Member("a", "receiver", "p", Role.CUSTOMER);
        chat1 = Chat.createPublicChat(sender, "chat1", 3);
        chat2 = Chat.createPrivateChat(sender, receiver);
        em.persist(sender);
        em.persist(receiver);
        em.persist(chat1);
        em.persist(chat2);
    }

    @AfterEach
    void tearDown() {
        em.clear();
    }

    @Test
    @DisplayName("채팅방 목록")
    void chatsByMember(){

        List<ChatMember> senderChats = chatMemberRepository.findByMemberOrderByChatLastModified(sender);
        List<ChatMember> receiverChats = chatMemberRepository.findByMemberOrderByChatLastModified(receiver);

        assertThat(senderChats.size()).isEqualTo(2);
        assertThat(receiverChats.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("채팅방 목록_정렬 기본값은 채팅생성역순")
    void chatsByMemberCreateOrdered(){

        List<ChatMember> senderChats = chatMemberRepository.findByMemberOrderByChatLastModified(sender);

        assertThat(senderChats.get(0).getChat()).isEqualTo(chat2);
        assertThat(senderChats.get(1).getChat()).isEqualTo(chat1);

    }

    @Test
    @DisplayName("채팅방 목록_최우선은 마지막 메세지 역순")
    void chatsByMemberMessageOrdered(){

        em.persist(new Message(chat1, sender, "last message"));

        List<ChatMember> senderChats = chatMemberRepository.findByMemberOrderByChatLastModified(sender);

        assertThat(senderChats.get(0).getChat()).isEqualTo(chat1);
        assertThat(senderChats.get(1).getChat()).isEqualTo(chat2);

    }


    @Test
    void findByChatAndMember() {
        em.flush();
        em.detach(chat1);
        em.detach(sender);
        ChatMember chatMember = chatMemberRepository.findByChatAndMember(chat1.getId(), sender.getId()).get();
        System.out.println("chatMember.getChat().getTitle() = " + chatMember.getChat().getTitle());
        System.out.println("chatMember.getMember().getUsername() = " + chatMember.getMember().getUsername());
    }


}