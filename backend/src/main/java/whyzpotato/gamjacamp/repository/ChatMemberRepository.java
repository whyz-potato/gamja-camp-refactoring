package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

//    @Query("SELECT cm FROM ChatMember cm " +
//            "JOIN Chat c ON cm.chat = c " +
//            "WHERE cm.member = :member " +
//            "ORDER BY c.last_message desc")
//    List<ChatMember> findByMemberOrderByLastMessage(Member member);

    Optional<ChatMember> findByChatAndMember(Chat chat, Member member);


}
