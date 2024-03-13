package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whyzpotato.gamjacamp.domain.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(
            nativeQuery = true,
            value = "select r.*" +
                    " from room r" +
                    " left join (" +
                    "       select room_id, count(*) as cnt " +
                    "       from reservation" +
                    "       where stay_starts < :end AND stay_ends > :start" +
                    "       group by room_id" +
                    ") reserved on r.room_id = reserved.room_id" +
                    " where r.camp_id = :camp_id and r.capacity >= :numGuests and r.cnt > IFNULL(reserved.cnt, 0)"
    )
    List<Room> findAvailRooms(@Param("camp_id") Long campId, @Param("start") LocalDate checkIn, @Param("end") LocalDate checkOut, @Param("numGuests") int numGuests);

    @Query(
            nativeQuery = true,
            value = "select r.cnt - (select count(*)" +
                    "               from reservation" +
                    "               where room_id = :room_id and stay_starts < :end AND stay_ends > :start)" +
                    " from room r" +
                    " where r.room_id = :room_id and r.capacity >= :numGuests"
    )
    Long countAvailRoom(@Param("room_id") Long roomId, @Param("start") LocalDate checkIn, @Param("end") LocalDate checkOut, @Param("numGuests") int numGuests);
}
