package whyzpotato.gamjacamp.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.ScrapCamp;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ScrapCampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    private Member host;
    private Member visitor;
    private Camp camp;

    @BeforeEach
    void setUp() {
        host = new Member("customer@potato.com", "host", "", Role.OWNER);
        visitor = new Member("a", "visitor", "", Role.CUSTOMER);
        camp = Camp.builder().member(host).name("tester camp").address("경기도 남양주시").longitude(1.1).latitude(1.1).build();
        em.persist(host);
        em.persist(visitor);
        em.persist(camp);

        session = new MockHttpSession();
    }

    @AfterEach
    void tearDown() {
        em.clear();
        session.clearAttributes();
    }


    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @DisplayName("캠프 스크랩 생성")
    public void postCampScrap() throws Exception {

        session.setAttribute("member", new SessionMember(visitor));

        mockMvc.perform(post("/customer/my-camp")
                        .session(session)
                        .param("camp", String.valueOf(camp.getId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @DisplayName("캠프 스크랩 조회")
    public void getCampScraps() throws Exception {

        session.setAttribute("member", new SessionMember(visitor));
        em.persist(new ScrapCamp(visitor, camp));

        mockMvc.perform(get("/customer/my-camp")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.isLast").value(true))
                .andExpect(jsonPath("$.content[0].id").value(camp.getId()))
                .andDo(print());
    }


}
