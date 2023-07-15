package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.ChatMemberRepository;
import whyzpotato.gamjacamp.repository.ChatRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.MessageRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;

    public void updateLastReadMessage(Long chatId, Long memberId, Long messageId){

        Member member = memberRepository.findById(memberId).get();
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException());

        ChatMember chatMember = chatMemberRepository.findByChatAndMember(chat, member).get();
        chatMember.updateLastReadMessage(message);

    }

}
