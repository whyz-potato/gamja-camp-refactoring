package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.querydto.CampSearchResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CampRepository extends JpaRepository<Camp, Long> {
    Optional<Camp> findByMember(Member member);

    @Query(
            nativeQuery = true,
            value = "SELECT"
                    + " c.camp_id, c.name, c.address, r.capacity AS capacity,"
//                    + " (r.cnt - count(r.room_id) as numAvail"
                    + " FROM camp c"
                    + " JOIN room r"
                    + " ON c.camp_id = r.camp_id"
                    + " LEFT OUTER JOIN (SELECT * FROM reservation r"
                    + "                  WHERE r.stay_starts < :end AND r.stay_ends > :start) b"
                    + " ON r.room_id = b.room_id"
//                    + " GROUP BY r.room_id"
//                    + " HAVING numAvail > 0"
                    + " WHERE capacity >= :capacity")
    List<CampSearchResult> findAvailableCamp(@Param("start") LocalDate start, @Param("end") LocalDate end,
                                             @Param("capacity") int capacity);

}