package whyzpotato.gamjacamp.service;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private Member owner, customer;
    private Camp camp;
    private Room room;
    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        owner = Member.builder().account("owner@mail.com").role(Role.OWNER).username("owner").picture("img").build();
        customer = Member.builder().account("customer@mail.com").role(Role.CUSTOMER).username("customer").picture("img").build();
        camp = Camp.builder().member(owner).name("tester camp").address("경기도 남양주시").longitude(1.1).latitude(1.1).build();
        room = Room.builder().camp(camp).name("room").cnt(4).capacity(3).weekendPrice(1000).weekPrice(1200).build();
        em.persist(owner);
        em.persist(customer);
        em.persist(camp);
        em.persist(room);

        //Member member, Camp camp, Room room, int numGuest, LocalDate stayStarts, LocalDate stayEnds, List<Integer> prices
        reservations = new ArrayList<>();
        reservations.add(Reservation.builder().member(customer).camp(camp).room(room).numGuest(2).stayStarts(LocalDate.now()).stayEnds(LocalDate.now().plusDays(1)).prices(room.getPrices(LocalDate.now(), LocalDate.now().plusDays(1))).build());
        reservations.add(Reservation.builder().member(customer).camp(camp).room(room).numGuest(2).stayStarts(LocalDate.now().plusDays(3)).stayEnds(LocalDate.now().plusDays(5)).prices(room.getPrices(LocalDate.now().plusDays(3), LocalDate.now().plusDays(5))).build());
        reservations.add(Reservation.builder().member(customer).camp(camp).room(room).numGuest(2).stayStarts(LocalDate.now().plusDays(7)).stayEnds(LocalDate.now().plusDays(10)).prices(room.getPrices(LocalDate.now().plusDays(7), LocalDate.now().plusDays(10))).build());
        for (Reservation reservation : reservations) {
            em.persist(reservation);
        }
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

        Reservation reservation = reservationService.createReservation(customer, camp, room, numGuest, start, end, prices);

        assertThat(reservation.getId()).isNotNull();
    }

    @DisplayName("객실 예약 DTO 성공 -> 영속엔티티 생성")
    @Test
    public void reserveDto() {
        LocalDate start = LocalDate.now(), end = LocalDate.now().plusDays(1);
        List<Integer> prices = room.getPrices(start, end);
        int numGuest = 2;
        ReservationRequest dto = new ReservationRequest(camp.getId(), room.getId(), start, end, new MemberDto.MemberSimple(customer), new ReservationDto.ReservationSimple(numGuest, List.copyOf(room.getPrices(start, end))));

        Long reservationId = reservationService.createReservation(customer.getId(), dto);

        assertThat(reservationId).isNotNull();

    }


    @DisplayName("객실 예약 성공 -> 정보 확인")
    @Test
    public void reserve_success2() {
        LocalDate start = LocalDate.now(), end = LocalDate.now().plusDays(1);
        List<Integer> prices = room.getPrices(start, end);
        int numGuest = 2;

        Reservation reservation = reservationService.createReservation(customer, camp, room, numGuest, start, end, prices);

        assertThat(reservation.getPrice()).isEqualTo(prices.stream().mapToInt(Integer::intValue).sum());
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PENDING);
    }

    @DisplayName("객실 예약 실패 <- 객실이 부족한 경우 예약할 수 있다")
    @Test
    public void reserve_fullReserve_fail() {
        //TODO

    }

    @DisplayName("객실 예약 실패 <- 인원 수는 수용가능인원 이하여야 한다.")
    @Test
    public void reserve_wrongNumGuests_fail() {
        //given
        LocalDate start = LocalDate.now(), end = LocalDate.now().plusDays(1);
        List<Integer> prices = room.getPrices(start, end);

        //when - then
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.createReservation(customer, camp, room, room.getCapacity() + 1, start, end, prices));
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
                () -> reservationService.createReservation(customer, camp, room, room.getCapacity() - 1, start, end, prices));

    }

    @DisplayName("객실 예약 수락")
    @Test
    public void updateStatusBooked() {

        List<Long> ids = reservations.stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());

        reservationService.updateStatus(owner.getId(), ReservationStatus.BOOKED, ids);

        assertThat(reservations.get(2).getStatus()).isEqualTo(ReservationStatus.BOOKED);
        assertThat(em.find(Reservation.class, reservations.get(0).getId()).getStatus()).isEqualTo(ReservationStatus.BOOKED);
    }

    @DisplayName("객실 예약 거절")
    @Test
    public void updateStatusCanceled() {

        List<Long> ids = reservations.stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());

        reservationService.updateStatus(owner.getId(), ReservationStatus.CANCELED, ids);

        assertThat(reservations.get(2).getStatus()).isEqualTo(ReservationStatus.CANCELED);
        assertThat(em.find(Reservation.class, reservations.get(0).getId()).getStatus()).isEqualTo(ReservationStatus.CANCELED);
    }

    @DisplayName("객실 예약 취소")
    @Test
    public void cancel(){

        Reservation reservationAfterWeek = reservations.get(2);

        reservationService.cancel(customer.getId(), reservationAfterWeek.getId());

        assertThat(reservationAfterWeek.getStatus()).isEqualTo(ReservationStatus.CANCELED);

    }

    @DisplayName("객실예약취소_3일이내_실패")
    @Test
    public void cancelLate(){

        Reservation reservation = reservations.get(0);

        assertThrows(IllegalStateException.class,
                () -> reservationService.cancel(customer.getId(), reservation.getId()));

    }

}