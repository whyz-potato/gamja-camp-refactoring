package whyzpotato.gamjacamp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whyzpotato.gamjacamp.controller.dto.Utility;
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
                    + " c.camp_id, c.name, c.address, r.capacity"
//                    + " (r.cnt - count(r.room_id) as numAvail"
                    + " FROM camp c"
                    + " JOIN room r"
                    + " ON c.camp_id = r.camp_id"
                    + " LEFT OUTER JOIN (SELECT * FROM reservation r"
                    + "                  WHERE r.stay_starts < :end AND r.stay_ends > :start) b"
                    + " ON r.room_id = b.room_id"
//                    + " GROUP BY  r.room_id"
//                    + " HAVING numAvail > 0"
                    + " WHERE r.capacity >= :capacity and (c.name like %:query% or c.address like %:query%)",
            countProjection = "c.camp_id")
    Page<CampSearchResult> findAvailableCamp(@Param("query") String query, @Param("start") LocalDate start, @Param("end") LocalDate end,
                                             @Param("capacity") int capacity, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT"
                    + " c.camp_id, MIN(r.capacity) as capacity"//, c.name, c.address, MIN(r.capacity)"
//                    + " (r.cnt - count(r.room_id) AS numAvail"
                    + " FROM camp c"
                    + " JOIN room r"
                    + " ON c.camp_id = r.camp_id"
                    + " LEFT OUTER JOIN (SELECT * FROM reservation r"
                    + "                  WHERE r.stay_starts < :end AND r.stay_ends > :start) b"
                    + " ON r.room_id = b.room_id"
                    + " GROUP BY c.camp_id"
                    + " HAVING capacity >= :capacity"
                    + " WHERE :query")// and (c.name like %:query% or c.address like %:query%)")
    List<CampSearchResult> findAvailableCampList(@Param("query") String query, @Param("start") LocalDate start, @Param("end") LocalDate end,
                                                 @Param("capacity") int capacity);

}
