package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.chat.ChatMember;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

}
