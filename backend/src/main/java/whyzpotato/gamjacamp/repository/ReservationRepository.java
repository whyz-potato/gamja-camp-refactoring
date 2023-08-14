package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

}
