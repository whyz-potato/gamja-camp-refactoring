package whyzpotato.gamjacamp.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Slice<Message> findSliceByChat(Chat chat, Pageable pageable);

    Slice<Message> findSliceByChatAndIdLessThan(Chat chat, Long id, Pageable pageable);

}
