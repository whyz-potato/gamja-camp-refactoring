package whyzpotato.gamjacamp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.ReservationStatus;
import whyzpotato.gamjacamp.domain.member.Member;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByMemberOrderByStayStartsDesc(Member member, Pageable pageable);

    Page<Reservation> findByCampAndStatusOrderByStayStartsDesc(Camp camp, ReservationStatus status, Pageable pageable);

}
