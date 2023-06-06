package whyzpotato.gamjacamp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.service.MemberService;
import whyzpotato.gamjacamp.service.ReservationService;
import whyzpotato.gamjacamp.service.RoomService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private CampRepository campRepository;

    @MockBean
    private RoomService roomService;

    @BeforeEach
    void setUp() {

    }

//    @Test
//    public void reservationDetail(){
//        when(service.greet()).thenReturn("Hello, Mock");
//        this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
//                .andExpect(content().string(containsString("Hello, Mock")));
//
//    }

    @Test
    public void book(){

    }


}