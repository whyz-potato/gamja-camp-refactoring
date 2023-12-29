package whyzpotato.gamjacamp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.querydto.CampSearchResult;
import whyzpotato.gamjacamp.repository.querydto.RoomSearchResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CampRepository extends JpaRepository<Camp, Long> {

    Optional<Camp> findByMember(Member member);

    @Query(
            nativeQuery = true,
            value = "WITH RECURSIVE t_temp_dates(dt, wd) AS  (" +
                    "    SELECT :start, ISO_DAY_OF_WEEK(:start)" +
                    "    UNION ALL" +
                    "    SELECT" +
                    "        FORMATDATETIME(DATEADD(DAY, 1, dt), 'yyyy-MM-dd'), ISO_DAY_OF_WEEK(DATEADD(DAY, 1, dt))" +
                    "    FROM" +
                    "        t_temp_dates   " +
                    "    WHERE" +
                    "        DATEADD(DAY, 1, dt) < :end" +
                    ")" +
                    " SELECT c.camp_id, c.name, c.address, camp_price.min_price" +
                    " FROM camp c" +
                    " JOIN (" +
                    "     SELECT camp_id, MIN(price) as min_price " +
                    "     FROM (" +
                    "         SELECT r.room_id, r.camp_id, " +
                    "             SUM(CASE WHEN dates.wd < 6 THEN r.week_price" +
                    "                 ELSE r.weekend_price" +
                    "                 END) AS price" +
                    "         FROM room r" +
                    "         LEFT JOIN (" +
                    "             SELECT room_id, COUNT(*) AS cnt" +
                    "             FROM reservation" +
                    "             WHERE stay_starts < :end AND stay_ends >:start" +
                    "             GROUP BY room_id " +
                    "         ) reserved " +
                    "         ON r.room_id = reserved.room_id" +
                    "         CROSS JOIN t_temp_dates dates" +
                    "         WHERE r.capacity>=:capacity AND r.cnt > IFNULL(reserved.cnt, 0)" +
                    "         GROUP BY r.room_id, r.camp_id" +
                    "     )" +
                    "     GROUP BY camp_id " +
                    " ) camp_price" +
                    " ON c.camp_id = camp_price.camp_id" +
                    " WHERE c.name LIKE %:query% OR c.address LIKE %:query%",
            countProjection = "c.camp_id")
    Page<CampSearchResult> searchAvailCamp(@Param("query") String query,
                                           @Param("start") LocalDate start,
                                           @Param("end") LocalDate end,
                                           @Param("capacity") int capacity,
                                           Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "WITH RECURSIVE t_temp_dates(dt, wd) AS  (" +
                    "    SELECT :start, ISO_DAY_OF_WEEK(:start)" +
                    "    UNION ALL" +
                    "    SELECT" +
                    "        FORMATDATETIME(DATEADD(DAY, 1, dt), 'yyyy-MM-dd'), ISO_DAY_OF_WEEK(DATEADD(DAY, 1, dt))" +
                    "    FROM" +
                    "        t_temp_dates   " +
                    "    WHERE" +
                    "        DATEADD(DAY, 1, dt) < :end" +
                    ")" +
                    " SELECT c.camp_id, c.name, c.address, camp_price.min_price" +
                    " FROM camp c" +
                    " JOIN (" +
                    "     SELECT camp_id, MIN(price) as min_price " +
                    "     FROM (" +
                    "         SELECT r.room_id, r.camp_id, " +
                    "             SUM(CASE WHEN dates.wd < 6 THEN r.week_price" +
                    "                 ELSE r.weekend_price" +
                    "                 END) AS price" +
                    "         FROM room r" +
                    "         LEFT JOIN (" +
                    "             SELECT room_id, COUNT(*) AS cnt" +
                    "             FROM reservation" +
                    "             WHERE stay_starts < :end AND stay_ends >:start" +
                    "             GROUP BY room_id " +
                    "         ) reserved " +
                    "         ON r.room_id = reserved.room_id" +
                    "         CROSS JOIN t_temp_dates dates" +
                    "         WHERE r.capacity>=:capacity AND r.cnt > IFNULL(reserved.cnt, 0)" +
                    "         GROUP BY r.room_id, r.camp_id" +
                    "     )" +
                    "     GROUP BY camp_id " +
                    " ) camp_price" +
                    " ON c.camp_id = camp_price.camp_id" +
                    " WHERE c.name LIKE %:query% OR c.address LIKE %:query%")
    List<CampSearchResult> searchAvailCampList(@Param("query") String query,
                                               @Param("start") LocalDate start,
                                               @Param("end") LocalDate end,
                                               @Param("capacity") int capacity);

    @Query(
            nativeQuery = true,
            value = "WITH RECURSIVE t_temp_dates(dt, wd) AS  (" +
                    "    SELECT :start, ISO_DAY_OF_WEEK(:start)" +
                    "    UNION ALL" +
                    "    SELECT" +
                    "        FORMATDATETIME(DATEADD(DAY, 1, dt), 'yyyy-MM-dd'), ISO_DAY_OF_WEEK(DATEADD(DAY, 1, dt))" +
                    "    FROM" +
                    "        t_temp_dates   " +
                    "    WHERE" +
                    "        DATEADD(DAY, 1, dt) < :end" +
                    ")" +
                    " SELECT r.room_id, r.camp_id, " +
                    "        SUM(CASE WHEN dates.wd < 6 THEN r.week_price " +
                    "                 ELSE r.weekend_price " +
                    "            END) AS price " +
                    " FROM room r" +
                    " LEFT JOIN ( " +
                    "    SELECT room_id, COUNT(*) AS cnt " +
                    "    FROM reservation " +
                    "    WHERE stay_starts < :end AND stay_ends >:start " +
                    "    GROUP BY room_id  " +
                    ") reserved  " +
                    " ON r.room_id = reserved.room_id " +
                    " CROSS JOIN t_temp_dates dates" +
                    " WHERE r.capacity>= :capacity AND r.cnt > IFNULL(reserved.cnt, 0) " +
                    " GROUP BY r.room_id, r.camp_id")
    List<RoomSearchResult> findAllRoom(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("capacity") int capacity);

}
