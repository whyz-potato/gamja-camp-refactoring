package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.PeakPrice;

public interface PeakPriceRepository extends JpaRepository<PeakPrice, Long> {
}
