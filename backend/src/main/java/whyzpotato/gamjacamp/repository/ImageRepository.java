package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Image;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByFileName(String fileName);
    void deleteByCamp(Long campId);
    List<Image> findAllByCamp(Camp camp);
}