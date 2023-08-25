package whyzpotato.gamjacamp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.MemberDto;
import whyzpotato.gamjacamp.controller.dto.ReservationDto;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationRequest;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.ReservationStatus;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @PersistenceContext
    private EntityManager em;

    private Member member;
    private Camp camp;
    private Room room;

    @BeforeEach
    void setUp() {
        member = Member.builder().account("account@mail.com").role(Role.OWNER).username("tester").picture("img").build();
        em.persist(member);
        camp = Camp.builder().member(member).name("tester camp").address("경기도 남양주시").longitude(1.1).latitude(1.1).build();
        em.persist(camp);
        room = Room.builder().camp(camp).name("room").cnt(2).capacity(3).weekendPrice(1000).weekPrice(1200).build();
        em.persist(room);
    }

    @AfterEach
    void tearDown() {
        em.clear();
    }

    @DisplayName("객실 예약 성공 -> 영속엔티티 생성")
    @Test
    public void reserve() {
        LocalDate start = LocalDate.now(), end = LocalDate.now().plusDays(1);
        List<Integer> prices = room.getPrices(start, end);
        int numGuest = 2;

        Reservation reservation = reservationService.createReservation(member, camp, room, numGuest, start, end, prices);

        assertThat(reservation.getId()).isNotNull();
    }

    @DisplayName("객실 예약 DTO 성공 -> 영속엔티티 생성")
    @Test
    public void reserveDto() {
        LocalDate start = LocalDate.now(), end = LocalDate.now().plusDays(1);
        List<Integer> prices = room.getPrices(start, end);
        int numGuest = 2;
        ReservationRequest dto = new ReservationRequest(camp.getId(), room.getId(), start, end, new MemberDto.MemberSimple(member), new ReservationDto.ReservationSimple(numGuest, List.copyOf(room.getPrices(start, end))));

        Long reservationId = reservationService.createReservation(member.getId(), dto);

        assertThat(reservationId).isNotNull();

    }



    @DisplayName("객실 예약 성공 -> 정보 확인")
    @Test
    public void reserve_success2() {
        LocalDate start = LocalDate.now(), end = LocalDate.now().plusDays(1);
        List<Integer> prices = room.getPrices(start, end);
        int numGuest = 2;

        Reservation reservation = reservationService.createReservation(member, camp, room, numGuest, start, end, prices);

        assertThat(reservation.getPrice()).isEqualTo(prices.stream().mapToInt(Integer::intValue).sum());
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PENDING);
    }

    @DisplayName("객실 예약 실패 <- 객실이 부족한 경우 예약할 수 있다")
    @Test
    public void reserve_fullReserve_fail() {
        //TODO
        //illegalStatementException

    }

    @DisplayName("객실 예약 실패 <- 인원 수는 수용가능인원 이하여야 한다.")
    @Test
    public void reserve_wrongNumGuests_fail() {
        //given
        LocalDate start = LocalDate.now(), end = LocalDate.now().plusDays(1);
        List<Integer> prices = room.getPrices(start, end);

        //when - then
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.createReservation(member, camp, room, room.getCapacity() + 1, start, end, prices));
    }

    @DisplayName("객실 예약 실패 <- 검색과 저장 사이에 가격변동이 발생한 경우 예약할 수 없다")
    @Test
    public void reserve_modifiedPrice_fail() {
        //given
        LocalDate start = LocalDate.now(), end = LocalDate.now().plusDays(1);
        List<Integer> prices = room.getPrices(start, end);

        //when
        room.setWeekPrice(10);
        room.setWeekendPrice(100);

        //then
        assertThrows(IllegalStateException.class,
                () -> reservationService.createReservation(member, camp, room, room.getCapacity() - 1, start, end, prices));

    }

}