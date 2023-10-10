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
    private final ChatMemberService chatMemberService;

    private PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdTime"));

    // (채팅방 메세지 전송) 전송 및 읽음 처리
    public DetailMessageDto createMessage(Long chatId, Long memberId, String content) {
        Chat chat = chatRepository.findById(chatId).get();
        Member sender = memberRepository.findById(memberId).get();

        Message savedMessage = messageRepository.save(new Message(chat, sender, content));

        chatMemberService.updateLastReadMessage(chatId, memberId, savedMessage.getId());

        return new DetailMessageDto(savedMessage);
    }

    // (채팅방 윈도우 오픈) 가장 최근 메세지 반환 및 읽음 처리
    public MessageListDto findMessages(Long chatId, Long memberId) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();

        if (!chatMemberRepository.existsByChatAndMember(chat, member)) {
            throw new NotFoundException();
        }

        Slice<Message> slice = messageRepository.findSliceByChat(chat, pageRequest);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));

        chatMemberService.updateLastReadMessage(chatId, memberId, result.getContent().get(0).getId());

//        // 메세지 슬라이스 내부에서 역순으로 전달
//        result.getContent().sort(Comparator.comparing(Message::getCreatedTime, Comparator.naturalOrder())); # E :UnmodifiableList.sort
        return new MessageListDto(result);
    }

    public MessageListDto findMessages(Long chatId, Long memberId, Long start) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();

        if (!chatMemberRepository.existsByChatAndMember(chat, member)) {
            throw new NotFoundException();
        }

        Slice<Message> slice = messageRepository.findSliceByChatAndIdLessThan(chat, start, pageRequest);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));
        return new MessageListDto(result);
    }

}
