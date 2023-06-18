package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}