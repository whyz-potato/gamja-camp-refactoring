package whyzpotato.gamjacamp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.ScrapCamp;
import whyzpotato.gamjacamp.domain.member.Member;

public interface ScrapCampRepository extends JpaRepository<ScrapCamp, Long> {

    Page<ScrapCamp> findByMember(Member member, Pageable pageable);

}
