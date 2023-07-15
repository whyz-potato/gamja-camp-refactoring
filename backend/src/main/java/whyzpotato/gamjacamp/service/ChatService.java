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
import whyzpotato.gamjacamp.repository.ChatRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.MessageRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;

    public Chat createPublicChat(Long hostId, String title, int capacity) {
        Member host = memberRepository.findById(hostId).get();
        return chatRepository.save(Chat.createPublicChat(host, title, capacity));
    }

    public Chat createPrivateChat(Long hostId, Long customerId) {
        Member host = memberRepository.findById(hostId).get();
        Member customer = memberRepository.findById(customerId).get();
        return chatRepository.save(Chat.createPrivateChat(host, customer));
    }

    public Chat enter(Long chatId, Long memberId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();
        return chat.enter(member);
    }

    public MessageListDto findMessages(Long chatId, Long memberId) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();

        if (!chat.isParticipant(member)) {
            throw new NotFoundException();
        }

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "message_id"));
        Slice<Message> slice = messageRepository.findSliceByChat(chat, pageRequest);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));

        return new MessageListDto(result);
    }

    public MessageListDto findMessages(Long chatId, Long memberId, Long start) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();

        if (!chat.isParticipant(member)) {
            throw new NotFoundException();
        }

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "message_id"));
        Slice<Message> slice = messageRepository.findSliceByChatAndIdLessThan(chat, start, pageRequest);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));

        return new MessageListDto(result);
    }


}
