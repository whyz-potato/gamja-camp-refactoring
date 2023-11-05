package whyzpotato.gamjacamp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.repository.querydto.CampSearchResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CampRepositoryTest {

    @Autowired
    private CampRepository campRepository;

    @PersistenceContext
    private EntityManager em;

    private Member host;
    private Camp camp1;
    private Camp camp2;
    private Room room1;
    private Room room2;
    private Room room3;
    private Room room4;


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
        room2 = Room.builder().name("camp1_room2").camp(camp1).capacity(5).cnt(1).weekPrice(7000).weekendPrice(10000)
                .build();
        room3 = Room.builder().name("camp2_room3").camp(camp2).capacity(2).cnt(1).weekPrice(5000).weekendPrice(7000)
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

    @Rollback(false)
    @Test
    public void 캠핑장_검색() {
//        campRepository.findAvailableCamp(LocalDate.now(), LocalDate.now(), 1, PageRequest.of(10, 10));
        List<CampSearchResult> availableCamp = campRepository.findAvailableCamp(LocalDate.now(), LocalDate.now(), 4);

        assertThat(availableCamp.size()).isEqualTo(2);
        assertThat(availableCamp).extracting("capacity").containsOnly(5);

    }
}