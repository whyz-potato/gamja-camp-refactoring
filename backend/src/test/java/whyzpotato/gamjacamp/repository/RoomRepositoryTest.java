package whyzpotato.gamjacamp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class RoomRepositoryTest {
    @Autowired
    private RoomRepository roomRepository;

    @PersistenceContext
    private EntityManager em;

    private Camp camp;
    private Room room1;
    private Room room2;
    private Room room3;
    private Room room4;
    private LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {

        Member host = Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(
                Role.OWNER).build();
        Member customer = Member.builder().account("customer@mail.com").role(Role.CUSTOMER).username("customer").picture("img")
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

    @DisplayName("특정 캠핑장의 예약 가능한 객실 조회 - 예약이 없는 경우 모든 객실 반환")
    @Test
    void findAvailRooms_noReservation() {
        List<Room> availRooms = roomRepository.findAvailRooms(camp.getId(), today, today.plusDays(1), 2);

        Assertions.assertThat(availRooms).contains(room1, room2, room3, room4);
    }

    @DisplayName("인원 수를 만족하는 객실만 반환")
    @Test
    void findAvailCapacityRooms() {
        List<Room> availRooms = roomRepository.findAvailRooms(camp.getId(), today, today.plusDays(1), 4);

        Assertions.assertThat(availRooms).containsOnly(room3, room4);
    }

    @DisplayName("예약가능한 객실 조회")
    @Test
    void findAvailRooms() {
        List<Room> availRooms = roomRepository.findAvailRooms(camp.getId(), today.plusDays(3), today.plusDays(4), 2);

        Assertions.assertThat(availRooms).containsOnly(room2, room4);
    }
}
