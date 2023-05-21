package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.ScrapCamp;

public interface ScrapCampRepository extends JpaRepository<ScrapCamp, Long> {
}
