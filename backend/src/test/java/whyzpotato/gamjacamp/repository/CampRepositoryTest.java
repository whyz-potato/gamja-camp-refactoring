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
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.repository.querydto.CampSearchResult;

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
    Room room1;
    Room room2;
    Room room3;
    Room room4;
    Pageable pageable = PageRequest.of(0, 10);
    LocalDate start = LocalDate.parse("2023-12-29");
    LocalDate end = LocalDate.parse("2023-12-31");

    @BeforeEach
    void setUp() {

        host = Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(
                Role.OWNER).build();
        camp1 = Camp.builder().member(host).name("감자캠핑1").address("서울특별시 동일로40길 25-1").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build();
        camp2 = Camp.builder().member(host).name("감자캠핑2").address("서울특별시 광진구").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build();
        room1 = Room.builder().name("camp1_room1").camp(camp1).capacity(2).cnt(2).weekPrice(7000).weekendPrice(10000)
                .build();
        room2 = Room.builder().name("camp1_room2").camp(camp1).capacity(3).cnt(1).weekPrice(7000).weekendPrice(10000)
                .build();
        room3 = Room.builder().name("camp2_room3").camp(camp2).capacity(4).cnt(1).weekPrice(5000).weekendPrice(7000)
                .build();
        room4 = Room.builder().name("camp2_room4").camp(camp2).capacity(5).cnt(1).weekPrice(5000).weekendPrice(7000)
                .build();

        em.persist(host);
        em.persist(camp1);
        em.persist(camp2);
        em.persist(room1);
        em.persist(room2);
        em.persist(room3);
        em.persist(room4);
    }

    @AfterEach
    void tearDown() {
        em.clear();
    }

    @DisplayName("캠핑장 검색_전체")
    @Test
    public void search() {
        Page<CampSearchResult> availableCamp = campRepository.searchAvailCamp("", start, end, 1, pageable);

        System.out.println(availableCamp.getContent().size());

        assertThat(availableCamp.getTotalElements()).isEqualTo(2);
        assertThat(availableCamp.getContent().size()).isEqualTo(2);
    }

    @DisplayName("캠핑장 검색_예약가능한 객실이 여러개라도 캠핑장 하나씩만 반환되야한다.")
    @ParameterizedTest
    @CsvSource(value = {"2,2", "4,1"})
    public void distinctCampSearch(int capacity, int expected) {
        Page<CampSearchResult> availableCamp = campRepository.searchAvailCamp("", start, end, capacity, pageable);

        availableCamp.forEach(c -> {
            System.out.println("c.campId = " + c.getCamp_Id());
            System.out.println(c.getName());
            System.out.println(c.getMin_Price());
        });
        assertThat(availableCamp.getTotalElements()).isEqualTo(expected);
        assertThat(availableCamp.getNumberOfElements()).isEqualTo(expected);
        assertThat(availableCamp.getContent().stream().map(CampSearchResult::getCamp_Id).distinct().count()).isEqualTo(expected);
    }
}
