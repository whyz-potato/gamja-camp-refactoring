package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.ChatRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public Chat createPublicChat(Long hostId, String title, int capacity) {
        Member host = memberRepository.findById(hostId).get();
        return chatRepository.save(Chat.createPublicChat(host, title, capacity));
    }

    public Chat createPrivateChat(Long hostId, Long customerId) {
        Member host = memberRepository.findById(hostId).get();
        Member customer = memberRepository.findById(customerId).get();
        return chatRepository.save(Chat.createPrivateChat(host, customer));
    }


}
