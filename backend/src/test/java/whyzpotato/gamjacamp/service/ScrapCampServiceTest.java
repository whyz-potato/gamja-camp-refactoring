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
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
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
//        em.clear();
    }


    @Rollback(value = false)
    @Test
    @DisplayName("캠핑장 스크랩")
    void createScrap() {

        ScrapCamp scrap = scrapCampService.createScrap(visitor.getId(), camp.getId());

        Assertions.assertThat(scrap.getId()).isNotNull();
    }


}