package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.DetailMessageDto;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageListDto;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.ChatMemberRepository;
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
    private final ChatMemberRepository chatMemberRepository;

    private PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdTime"));

    public DetailMessageDto createMessage(Long chatId, Long memberId, String content) {
        Chat chat = chatRepository.findById(chatId).get();
        Member sender = memberRepository.findById(memberId).get();

        Message savedMessage = messageRepository.save(new Message(chat, sender, content));

        return new DetailMessageDto(savedMessage);
    }

    public MessageListDto findMessages(Long chatId, Long memberId) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();

        if (!chatMemberRepository.existsByChatAndMember(chat, member)) {
            throw new NotFoundException();
        }

        Slice<Message> slice = messageRepository.findSliceByChat(chat, pageRequest);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));

        return new MessageListDto(result);
    }

    public MessageListDto findMessages(Long chatId, Long memberId, Long start) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();

        if (!chatMemberRepository.existsByChatAndMember(chat, member)) {
            throw new NotFoundException();
        }

        Slice<Message> slice = messageRepository.findSliceByChatAndIdLessThanEqual(chat, start, pageRequest);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));

        return new MessageListDto(result);
    }

}
