package whyzpotato.gamjacamp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ChatDto;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    private Member sender, receiver;

    @BeforeEach
    void setUp() {
        sender = new Member("customer@potato.com", "sender", "", Role.CUSTOMER);
        receiver = new Member("a", "receiver", "", Role.CUSTOMER);
        em.persist(sender);
        em.persist(receiver);

        session = new MockHttpSession();
        session.setAttribute("member", new SessionMember(sender));
    }

    @AfterEach
    void tearDown() {
        em.clear();
        session.clearAttributes();
    }

    @Test
    void createPrivateChat() throws Exception {

        String request = objectMapper.writeValueAsString(new ChatDto.PrivateChatRequest(receiver.getId()));

        mockMvc.perform(post("/chats/single").session(session)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    //TODO after post
    @Test
    void createPublicChat() throws Exception {

//        String request = objectMapper.writeValueAsString(new ChatDto.PublicChatRequest(receiver.getId()));
//
//        mockMvc.perform(post("/chats/group").session(session)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print());

    }

    @Test
    void enterChat() {
    }

    @Test
    void updateLastReadMessage() {
    }

    @Test
    void getMassages() throws Exception {
        Chat chat = Chat.createPrivateChat(sender, receiver);
        em.persist(chat);
        for (int i = 0; i < 20; i++) {
            em.persist(new Message(chat, sender, (i + 1) + "번 메세지"));
        }

        String uri = String.format("/chats/%d", chat.getId());
        mockMvc.perform(get(uri).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfElements").value(10))
                .andExpect(jsonPath("hasNext").value("true"))
                .andDo(print());


    }

    @Test
    void getMassagesFromStart() throws Exception {
        Chat chat = Chat.createPrivateChat(sender, receiver);
        em.persist(chat);
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Message message = new Message(chat, sender, (i + 1) + "번 메세지");
            em.persist(message);
            messages.add(message);
        }

        String fifthId = messages.get(5).getId().toString();

        String uri = String.format("/chats/%d", chat.getId());
        mockMvc.perform(get(uri).session(session)
                        .param("start", fifthId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfElements").value(6))
                .andExpect(jsonPath("hasNext").value("false"))
                .andDo(print());


    }

    @Test
    void chatList() {
    }

    @Test
    void messageSend() {
    }
}