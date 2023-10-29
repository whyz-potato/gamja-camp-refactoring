package whyzpotato.gamjacamp.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSimple;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.ScrapCamp;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

@SpringBootTest
@Transactional
class ScrapCampServiceTest {

    @Autowired
    private ScrapCampService scrapCampService;

    @PersistenceContext
    private EntityManager em;
    private Member host;
    private Member visitor;
    private Camp camp;

    @BeforeEach
    void setUp() {
        host = new Member("a", "host", "p", Role.OWNER);
        visitor = new Member("a", "visitor", "p", Role.CUSTOMER);
        camp = Camp.builder().member(host).name("tester camp").address("경기도 남양주시").longitude(1.1).latitude(1.1).build();
        em.persist(host);
        em.persist(visitor);
        em.persist(camp);
    }

    @AfterEach
    void tearDown() {
        em.clear();
    }


    @Test
    @DisplayName("캠핑장 스크랩")
    void createScrap() {

        ScrapCamp scrap = scrapCampService.createScrap(visitor.getId(), camp.getId());

        Assertions.assertThat(scrap.getId()).isNotNull();
    }

    @Test
    @DisplayName("캠핑장 스크랩 조회")
    void scraps() {

        Camp camp1 = Camp.builder().member(host).name("camp1").address("address").longitude(1.1).latitude(1.1).build();
        Camp camp2 = Camp.builder().member(host).name("camp2").address("address").longitude(1.1).latitude(1.1).build();
        Camp camp3 = Camp.builder().member(host).name("camp3").address("address").longitude(1.1).latitude(1.1).build();
        em.persist(camp1);
        em.persist(camp2);
        em.persist(camp3);
        em.persist(new ScrapCamp(visitor, camp1));
        em.persist(new ScrapCamp(visitor, camp2));
        em.persist(new ScrapCamp(visitor, camp3));

        Page<CampSimple> scraps = scrapCampService.scraps(visitor.getId(), PageRequest.of(0, 10));

        Assertions.assertThat(scraps.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(scraps.getContent().contains(new CampSimple(camp1)));

    }


}