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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CampRepositoryTest {

    private final Pageable pageable = PageRequest.of(0, 10);
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

    @DisplayName("캠핑장 검색_전체")
    @Test
    public void search() {
        Page<CampSearchResult> availableCamp = campRepository.findAvailableCamp("", LocalDate.now(), LocalDate.now(), 1, pageable);

        System.out.println(availableCamp.getContent().size());

        assertThat(availableCamp.getTotalElements()).isEqualTo(4);
        assertThat(availableCamp.getContent().size()).isEqualTo(4);
        assertThat(availableCamp.getContent()).extracting("capacity").containsOnly(5);
    }

    @DisplayName("캠핑장 검색_예약가능한 객실이 여러개라도 캠핑장 하나씩만 반환되야한다.")
    @ParameterizedTest
    @CsvSource(value = {"2,2", "4,2"})
    public void distinctCampSearch(int capacity, int expected) {
        Page<CampSearchResult> availableCamp = campRepository.findAvailableCamp("", LocalDate.now(), LocalDate.now(), capacity, pageable);

        availableCamp.forEach(c -> {
            System.out.println(c.getName());
            System.out.println(c.getCapacity());
        });
        assertThat(availableCamp.getTotalElements()).isEqualTo(expected);
        assertThat(availableCamp.getNumberOfElements()).isEqualTo(expected);
        assertThat(availableCamp.getContent().stream().map(CampSearchResult::getCampId).distinct().count()).isEqualTo(expected);
    }


    @DisplayName("검색 결과 리스트로 반환_캠핑장은 예약가능한 room이 여러개라도 하나씩만 반환되야한다.")
    @ParameterizedTest
    @CsvSource(value = {"2,2", "4,2"})
    public void distinctCampSearch_List(int capacity, int expected) {
        List<CampSearchResult> availableCamp = campRepository.findAvailableCampList("", LocalDate.now(), LocalDate.now(), capacity);

        availableCamp.forEach(c -> {
//            System.out.println(c.getName());
            System.out.println(c.getCapacity());
        });
        assertThat(availableCamp.size()).isEqualTo(expected);
    }

    @DisplayName("검색 결과 리스트로 반환_이름 또는 지역으로 검색")
    @ParameterizedTest
    @CsvSource(value = {"감자,2", "고구마,0", "서울,2", "부산,0"})
    public void 캠핑장_검색_리스트_query(String query, int expected) {
        List<CampSearchResult> availableCamp = campRepository.findAvailableCampList(query, LocalDate.now(), LocalDate.now(), 1);

        availableCamp.forEach(c -> {
//            System.out.println(c.getName());
            System.out.println(c.getCapacity());
        });
        assertThat(availableCamp.size()).isEqualTo(expected);
    }


}
