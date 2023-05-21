package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.chat.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
