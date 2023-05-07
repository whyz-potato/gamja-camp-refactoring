package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Camp;

public interface CampRepository extends JpaRepository<Camp, Long> {
}
