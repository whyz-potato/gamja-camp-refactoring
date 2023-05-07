package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.ScrapPost;

public interface ScrapPostRepository extends JpaRepository<ScrapPost, Long> {
}
