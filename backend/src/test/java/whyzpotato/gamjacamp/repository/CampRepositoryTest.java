package whyzpotato.gamjacamp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.repository.querydto.CampQueryDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CampRepositoryTest {

    @Autowired
    CampRepository campRepository;

    @PersistenceContext
    EntityManager em;

    Member host;
    Camp camp1;
    Camp camp2;
    Camp camp3;
    Camp camp4;
    Camp camp5;

    Pageable pageable = PageRequest.of(0, 10);
    LocalDate noReservationStart = LocalDate.now();
    LocalDate noReservationEnd = LocalDate.now().plusDays(2);
    LocalDate reservationStart = LocalDate.now().plusDays(3);
    LocalDate reservationEnd = LocalDate.now().plusDays(10);

    @BeforeEach
    void setUp() {
        host = Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(
                Role.OWNER).build();
        Member customer = Member.builder().account("customer@mail.com").role(Role.CUSTOMER).username("customer").picture("img")
                .build();
        em.persist(host);
        em.persist(customer);

        //camp
        camp1 = Camp.builder().member(host).name("감자캠핑1").address("서울특별시 동일로40길 25-1").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build();
        camp2 = Camp.builder().member(host).name("감자캠핑2").address("서울특별시 광진구").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build();
        camp3 = Camp.builder().member(host).name("감자캠핑3").address("부산광역시 중구").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build();
        camp4 = Camp.builder().member(host).name("감자캠핑4").address("경기도 구리시").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build();
        camp5 = Camp.builder().member(host).name("감자캠핑5").address("강원도 춘천시").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build();
        em.persist(camp1);
        em.persist(camp2);
        em.persist(camp3);
        em.persist(camp4);
        em.persist(camp5);

        //room : camp당 room2개
        Room camp1Room1 = Room.builder()
                .name("camp1_room1").camp(camp1).capacity(2).cnt(2).weekPrice(5000).weekendPrice(10000).build();
        Room camp1Room2 = Room.builder()
                .name("camp1_room2").camp(camp1).capacity(2).cnt(2).weekPrice(7000).weekendPrice(10000).build();
        Room camp2Room1 = Room.builder()
                .name("camp2_room1").camp(camp2).capacity(3).cnt(2).weekPrice(5000).weekendPrice(10000).build();
        Room camp2Room2 = Room.builder()
                .name("camp2_room1").camp(camp2).capacity(3).cnt(2).weekPrice(7000).weekendPrice(10000).build();
        Room camp3Room1 = Room.builder()
                .name("camp3_room1").camp(camp3).capacity(4).cnt(2).weekPrice(5000).weekendPrice(10000).build();
        Room camp3Room2 = Room.builder()
                .name("camp3_room2").camp(camp3).capacity(4).cnt(2).weekPrice(7000).weekendPrice(10000).build();
        Room camp4Room1 = Room.builder()
                .name("camp4_room1").camp(camp4).capacity(5).cnt(2).weekPrice(5000).weekendPrice(10000).build();
        Room camp4Room2 = Room.builder()
                .name("camp4_room2").camp(camp4).capacity(5).cnt(2).weekPrice(7000).weekendPrice(10000).build();
        Room camp5Room1 = Room.builder()
                .name("camp5_room1").camp(camp5).capacity(6).cnt(2).weekPrice(5000).weekendPrice(10000).build();
        Room camp5Room2 = Room.builder()
                .name("camp5_room2").camp(camp5).capacity(6).cnt(2).weekPrice(7000).weekendPrice(10000).build();
        em.persist(camp1Room1);
        em.persist(camp1Room2);
        em.persist(camp2Room1);
        em.persist(camp2Room2);
        em.persist(camp3Room1);
        em.persist(camp3Room2);
        em.persist(camp4Room1);
        em.persist(camp4Room2);
        em.persist(camp5Room1);
        em.persist(camp5Room2);

        //reservation
        //today ~ today+2 : NONE
        //today+3 ~ today+10 : camp1_room1(2), camp1_room2(2), camp2_room1(1)
        em.persist(Reservation.builder()
                .member(customer).camp(camp1).room(camp1Room1).numGuest(2).stayStarts(reservationStart)
                .stayEnds(reservationEnd).prices(camp1Room1.getPrices(reservationStart, reservationEnd)).build());
        em.persist(Reservation.builder()
                .member(customer).camp(camp1).room(camp1Room1).numGuest(2).stayStarts(reservationStart)
                .stayEnds(reservationEnd).prices(camp1Room1.getPrices(reservationStart, reservationEnd)).build());
        em.persist(Reservation.builder()
                .member(customer).camp(camp1).room(camp1Room2).numGuest(2).stayStarts(reservationStart)
                .stayEnds(reservationEnd).prices(camp1Room2.getPrices(reservationStart, reservationEnd)).build());
        em.persist(Reservation.builder()
                .member(customer).camp(camp1).room(camp1Room2).numGuest(2).stayStarts(reservationStart)
                .stayEnds(reservationEnd).prices(camp1Room2.getPrices(reservationStart, reservationEnd)).build());
        em.persist(Reservation.builder()
                .member(customer).camp(camp2).room(camp2Room1).numGuest(2).stayStarts(reservationStart)
                .stayEnds(reservationEnd).prices(camp2Room1.getPrices(reservationStart, reservationEnd)).build());
    }

    @AfterEach
    void tearDown() {
        em.clear();
    }

    @DisplayName("캠핑장 검색_전체")
    @Test
    public void searchAll() {
        Page<CampQueryDto> result = campRepository.searchAvailCamp("", noReservationStart, noReservationEnd, 1, 126.0, 127.0, 90.0, 93.0, pageable);

        System.out.println(result.getContent().size());

        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getContent().size()).isEqualTo(5);
    }

    @DisplayName("캠핑장 검색_이름 또는 주소로 검색")
    @ParameterizedTest
    @CsvSource(value = {"감자,5", "서울,2", "춘천,1"})
    public void querySearch(String query, int expectedCnt) {
        Page<CampQueryDto> result = campRepository.searchAvailCamp(query, noReservationStart, noReservationEnd, 1, 126.0, 127.0, 90.0, 93.0, pageable);

        assertThat(result.getTotalElements()).isEqualTo(expectedCnt);
    }

    @DisplayName("캠핑장 검색_예약가능한 캠핑장만 반환된다.")
    @Test
    public void searchDate() {
        Page<CampQueryDto> result = campRepository.searchAvailCamp("", reservationStart, reservationEnd, 1, 126.0, 127.0, 90.0, 93.0, pageable);

        assertThat(result.getContent()).extracting("name")
                .containsExactly("감자캠핑2", "감자캠핑3", "감자캠핑4", "감자캠핑5");
    }

    @DisplayName("캠핑장 검색_캠핑장 검색 시 예약가능한 객실 중 최저가 정보가 반환된다.")
    @Test
    public void searchMinPrice() {
        Page<CampQueryDto> result = campRepository.searchAvailCamp("", reservationStart, reservationEnd, 1, 126.0, 127.0, 90.0, 93.0, pageable);

        assertThat(result.getContent()).extracting("min_Price").containsOnly(45000);
    }

    @DisplayName("캠핑장 검색_페이징 테스트")
    @ParameterizedTest
    @CsvSource(value = {"0,3,3,", "2,2,1"})
    public void searchPageTest(int page, int size, int expectedNumElements) {
        Page<CampQueryDto> result = campRepository.searchAvailCamp("", noReservationStart, noReservationEnd, 1, 126.0, 127.0, 90.0, 93.0, PageRequest.of(page, size));

        assertThat(result.getNumberOfElements()).isEqualTo(expectedNumElements);
    }
}
