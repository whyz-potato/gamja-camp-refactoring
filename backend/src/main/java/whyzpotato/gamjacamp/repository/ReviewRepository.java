package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
