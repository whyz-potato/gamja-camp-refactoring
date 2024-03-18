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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.MemberDto;
import whyzpotato.gamjacamp.controller.dto.ReservationDto;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationRequest;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    private Member host, customer, reservedCustomer;
    private Camp camp;
    private Room room;
    private Reservation reservation1, reservationAfterWeek;
    private int weekPrice = 30000, weekendPrice = 50000;


    @BeforeEach
    void setUp() {

        session = new MockHttpSession();

        host = new Member("a", "host", "p", Role.OWNER);
        customer = new Member("a", "customer", "p", Role.CUSTOMER);
        reservedCustomer = new Member("a", "reservedCustomer", "p", Role.CUSTOMER);
        camp = Camp.builder()
                .member(host).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").phone("010-1234-1234").campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455)
                .build();
        room = Room.builder()
                .camp(camp).name("typeA").weekPrice(weekPrice).weekendPrice(weekendPrice).cnt(2).capacity(2)
                .build();

        em.persist(host);
        em.persist(customer);
        em.persist(reservedCustomer);
        em.persist(camp);
        em.persist(room);

        reservation1 = Reservation.builder()
                .member(reservedCustomer)
                .numGuest(2)
                .stayStarts(LocalDate.of(2023, 8, 21))
                .stayEnds(LocalDate.of(2023, 8, 23))
                .camp(camp)
                .room(room)
                .prices(room.getPrices(LocalDate.of(2023, 8, 21), LocalDate.of(2023, 8, 23)))
                .build();
        reservationAfterWeek = Reservation.builder()
                .member(reservedCustomer)
                .numGuest(2)
                .stayStarts(LocalDate.now().plusDays(7))
                .stayEnds(LocalDate.now().plusDays(8))
                .camp(camp)
                .room(room)
                .prices(room.getPrices(LocalDate.now().plusDays(7), LocalDate.now().plusDays(8)))
                .build();
        em.persist(reservation1);
        em.persist(reservationAfterWeek);
        em.persist(Reservation.builder()
                .member(reservedCustomer)
                .numGuest(2)
                .stayStarts(LocalDate.of(2023, 8, 26))
                .stayEnds(LocalDate.of(2023, 8, 27))
                .camp(camp)
                .room(room)
                .prices(room.getPrices(LocalDate.of(2023, 8, 26), LocalDate.of(2023, 8, 27)))
                .build());
        em.persist(Reservation.builder()
                .member(reservedCustomer)
                .numGuest(2)
                .stayStarts(LocalDate.of(2023, 9, 1))
                .stayEnds(LocalDate.of(2023, 9, 3))
                .camp(camp)
                .room(room)
                .prices(room.getPrices(LocalDate.of(2023, 9, 1), LocalDate.of(2023, 9, 3)))
                .build());

    }

    int getTodayPrice() {
        if (LocalDate.now().getDayOfWeek().getValue() < 6)
            return weekPrice;
        return weekendPrice;
    }

    @AfterEach
    void tearDown() {
        em.clear();
        session.clearAttributes();
    }


    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    public void reservationDetail() throws Exception {

        session.setAttribute("member", new SessionMember(reservedCustomer));


        mockMvc.perform(get("/customer/reservations/" + reservation1.getId()).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkIn").value(reservation1.getStayStarts().toString()))
                .andExpect(jsonPath("$.reservation.reservationDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.guest.name").value(reservedCustomer.getUsername()))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    public void book() throws Exception {

        session.setAttribute("member", new SessionMember(customer));
        String content = objectMapper.writeValueAsString(
                new ReservationRequest(camp.getId(), room.getId(), LocalDate.now(), LocalDate.now().plusDays(1),
                        new MemberDto.MemberSimple(customer),
                        new ReservationDto.ReservationSimple(2, List.of(getTodayPrice()))));

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/reservations")
                        .session(session)
                        .with(csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("/customer/reservations/*"))
                .andDo(print());


    }

    @Test
    @WithMockUser(username = "host", roles = "OWNER")
    public void changeStatusConfirm() throws Exception {

        session.setAttribute("member", new SessionMember(host));

//        String content = objectMapper.writeValueAsString(new StatusMultipleRequest(camp.getId(), ReservationStatus.BOOKED, List.of(reservation1.getId())));

        HashMap<String, Object> request = new HashMap<>();
        request.put("camp", camp.getId());
        request.put("status", "confirm");
        request.put("reservations", List.of(reservation1.getId()));
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/owner/reservations/status")
                        .session(session)
                        .with(csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owner/reservations"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    public void cancel() throws Exception {

        session.setAttribute("member", new SessionMember(reservedCustomer));
        String url = "/customer/reservations/" + reservationAfterWeek.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(url)
                        .session(session)
                        .with(csrf())
                )
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    public void cancelFail() throws Exception {

        session.setAttribute("member", new SessionMember(reservedCustomer));
        String url = "/customer/reservations/" + reservation1.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(url)
                        .session(session)
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void getReservationList() throws Exception {

        session.setAttribute("member", new SessionMember(reservedCustomer));
        String url = "/customer/reservations/my";

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .session(session)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.numberOfElements").value(4))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void getReservationPageableList() throws Exception {

        session.setAttribute("member", new SessionMember(reservedCustomer));
        String url = "/customer/reservations/my?page=1&size=3";

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .session(session)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "host", roles = "OWNER")
    void getPendingReservationList() throws Exception {

        session.setAttribute("member", new SessionMember(host));
        String url = "/owner/reservations";

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("status", "pending")
                        .session(session)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].stayStarts").value(reservationAfterWeek.getStayStarts().toString()))
                .andExpect(jsonPath("$.content[0].status").value("대기"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "host", roles = "OWNER")
    void getBookedReservationList() throws Exception {

        reservationAfterWeek.confirm(host);

        session.setAttribute("member", new SessionMember(host));
        String url = "/owner/reservations";

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("status", "booked")
                        .session(session)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].stayStarts").value(reservationAfterWeek.getStayStarts().toString()))
                .andExpect(jsonPath("$.content[0].status").value("확정"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "host", roles = "OWNER")
    void getCampReservationDetail() throws Exception {

        session.setAttribute("member", new SessionMember(host));
        String url = "/owner/reservations/" + reservation1.getId();

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .session(session)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkIn").exists())
                .andExpect(jsonPath("$.guest").exists())
                .andExpect(jsonPath("$.camp").exists())
                .andExpect(jsonPath("$.room").exists())
                .andExpect(jsonPath("$.guest.name").value(reservedCustomer.getUsername()))
                .andExpect(jsonPath("$.room.name").value(room.getName()))
                .andDo(print());

    }

    @Test
    @WithMockUser(username = "host", roles = "OWNER")
    void getCampReservation_NotHost_Fail() throws Exception {

        session.setAttribute("member", new SessionMember(reservedCustomer));
        String url = "/owner/reservations/" + reservation1.getId();

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .session(session)
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

}
