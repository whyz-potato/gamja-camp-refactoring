package whyzpotato.gamjacamp.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.Message;

import java.time.LocalDateTime;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Slice<Message> findSliceByChat(Chat chat, Pageable pageable);

    Slice<Message> findSliceByChatAndIdLessThan(Chat chat, Long id, Pageable pageable);

    Slice<Message> findSliceByChatAndIdGreaterThan(Chat chat, Long id, Pageable pageable);

    Slice<Message> findSliceByChatAndIdGreaterThanEqual(Chat chat, Long id, Pageable pageable);

    @Query("select count(*) from Message m where m.chat = :chat and m.createdTime > :lastReadDateTime")
    Long countByCreatedTimeAfter(@Param("chat") Chat chat, @Param("lastReadDateTime") LocalDateTime lastReadDateTime);

}
