package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.DetailMessageDto;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.ChatRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.MessageRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MessageService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;

    public DetailMessageDto createMessage(Long chatId, Long memberId, String content) {
        Chat chat = chatRepository.findById(chatId).get();
        Member sender = memberRepository.findById(memberId).get();

        Message savedMessage = messageRepository.save(new Message(chat, sender, content));

        return new DetailMessageDto(savedMessage);
    }

}
