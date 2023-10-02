package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.member.Member;

import java.util.Optional;

public interface CampRepository extends JpaRepository<Camp, Long> {
    Optional<Camp> findByMember(Member member);
}