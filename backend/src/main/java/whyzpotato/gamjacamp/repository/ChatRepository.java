package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.chat.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
