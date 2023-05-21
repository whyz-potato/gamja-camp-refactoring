package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
