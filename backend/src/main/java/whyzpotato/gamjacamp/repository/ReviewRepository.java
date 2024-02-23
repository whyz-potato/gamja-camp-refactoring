package whyzpotato.gamjacamp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Review;
import whyzpotato.gamjacamp.domain.member.Member;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReservation(Reservation reservation);

    Page<Review> findByIdLessThan(Long id, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE (r.writer = :writer AND r.id < :id)")
    Page<Review> findByWriterAndIdLessThan(@Param("writer") Member writer, @Param("id")Long id, Pageable pageable);

    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.camp = :camp")
    Double getRateAverage(@Param("camp") Camp camp);
}
