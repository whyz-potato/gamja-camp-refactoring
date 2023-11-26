package whyzpotato.gamjacamp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    private Member host;
    private Member customer;
    private Camp camp;
    private Room room1;
    private Room room2;
    private Room room3;
    private Room room4;
    private LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();

        host = Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build();
        customer = Member.builder().account("customer@mail.com").role(Role.CUSTOMER).username("customer").picture("img")
                .build();
        camp = Camp.builder().member(host).name("감자캠핑1").address("서울특별시 동일로40길 25-1").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build();
        room1 = Room.builder().name("room1").camp(camp).capacity(2).cnt(2).weekPrice(7_000).weekendPrice(10_000)
                .build();
        room2 = Room.builder().name("room2").camp(camp).capacity(2).cnt(2).weekPrice(8_000).weekendPrice(12_000)
                .build();
        room3 = Room.builder().name("room3").camp(camp).capacity(4).cnt(1).weekPrice(10_000).weekendPrice(15_000)
                .build();
        room4 = Room.builder().name("room4").camp(camp).capacity(6).cnt(1).weekPrice(15_000).weekendPrice(20_000)
                .build();
        em.persist(host);
        em.persist(customer);
        em.persist(camp);
        em.persist(room1);
        em.persist(room2);
        em.persist(room3);
        em.persist(room4);

        //today~ +1 : none

        //today+3 ~ today+5 : room1(2), room2(1), room3(1)
        LocalDate stayStarts = today.plusDays(3);
        LocalDate stayEnds = today.plusDays(5);
        em.persist(Reservation.builder()
                .member(customer).camp(camp).room(room1).numGuest(2).stayStarts(stayStarts).stayEnds(stayEnds)
                .prices(room1.getPrices(stayStarts, stayEnds)).build());
        em.persist(Reservation.builder()
                .member(customer).camp(camp).room(room1).numGuest(2).stayStarts(stayStarts).stayEnds(stayEnds)
                .prices(room1.getPrices(stayStarts, stayEnds)).build());
        em.persist(Reservation.builder()
                .member(customer).camp(camp).room(room2).numGuest(2).stayStarts(stayStarts).stayEnds(stayEnds)
                .prices(room2.getPrices(stayStarts, stayEnds)).build());
        em.persist(Reservation.builder()
                .member(customer).camp(camp).room(room3).numGuest(2).stayStarts(stayStarts).stayEnds(stayEnds)
                .prices(room3.getPrices(stayStarts, stayEnds)).build());
    }

    @AfterEach
    void tearDown() {
        em.clear();
        session.clearAttributes();
    }

    @DisplayName("예약 가능 객실 조회")
    @Test
    void availableRooms() throws Exception {
        String uri = "/rooms";
        mockMvc.perform(get(uri)
                        .param("camp", String.valueOf(camp.getId()))
                        .param("check-in", LocalDate.now().plusDays(3).toString())
                        .param("check-out", LocalDate.now().plusDays(6).toString())
                        .param("guests", String.valueOf(1))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.camp.name").value(camp.getName()))
                .andExpect(jsonPath("$.rooms.length()").value(2))
                .andExpect(jsonPath("$.rooms[*].price").exists())
                .andExpect(jsonPath("$.rooms[*].price.dailyPrices.length()", Matchers.everyItem(Matchers.is(3))))
                .andDo(print());
    }

    @DisplayName("예약 가능 객실 조회_파라미터 기본값 - 당일 1박, 2명")
    @Test
    void availableRooms_defaultParameter() throws Exception {
        String uri = "/rooms";
        mockMvc.perform(get(uri)
                        .param("camp", String.valueOf(camp.getId()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkIn").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.checkOut").value(LocalDate.now().plusDays(1).toString()))
                .andExpect(jsonPath("$.rooms.length()").value(4))
                .andExpect(jsonPath("$.rooms[*].capacity", Matchers.everyItem(Matchers.greaterThanOrEqualTo(2))))
                .andDo(print());
    }
}
