package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    @Query("SELECT cm FROM ChatMember cm" +
            " JOIN FETCH cm.chat c" +
            " JOIN FETCH cm.member m " +
            " WHERE cm.member = :member" +
            " ORDER BY c.lastModifiedTime DESC")
    List<ChatMember> findByMemberOrderByChatLastModified(@Param("member") Member member);

    Optional<ChatMember> findByChatAndMember(Chat chat, Member member);

    List<ChatMember> findByMember(Member member);

    boolean existsByChatAndMember(Chat chat, Member member);

    @Query("SELECT cm FROM ChatMember cm" +
            " join fetch cm.chat c" +
            " join fetch cm.member m " +
            " where c.id = :chatId and m.id = :memberId")
    Optional<ChatMember> findByChatAndMember(@Param("chatId") Long chatId, @Param("memberId") Long memberId);

}
