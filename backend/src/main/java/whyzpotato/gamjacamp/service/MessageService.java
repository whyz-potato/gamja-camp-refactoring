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
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.ChatMemberRepository;
import whyzpotato.gamjacamp.repository.ChatRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.MessageRepository;

import static whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageListDto.naturalOrderMessageList;
import static whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageListDto.reverseOrderMessageList;
import static whyzpotato.gamjacamp.utils.Utils.toSqlDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class MessageService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMemberService chatMemberService;

    private PageRequest backward = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdTime"));
    private PageRequest forward = PageRequest.of(0, 10);

    // (채팅방 메세지 전송) 전송 및 읽음 처리
    public DetailMessageDto createMessage(Long chatId, Long memberId, String content) {
        Chat chat = chatRepository.findById(chatId).get();
        Member sender = memberRepository.findById(memberId).get();

        Message savedMessage = messageRepository.save(new Message(chat, sender, content));

        chatMemberService.updateLastReadMessage(chatId, memberId, savedMessage.getId());

        return new DetailMessageDto(savedMessage);
    }


    public MessageListDto findMessages(Long chatId, Long memberId, int max) {

        ChatMember chatMember = chatMemberRepository.findByChatAndMember(chatId, memberId)
                .orElseThrow(IllegalArgumentException::new);
        Chat chat = chatMember.getChat();
        Member member = chatMember.getMember();

        //최근 메세지 반환
        if (chatMember.getLastReadMessage() == null || countUnreadMessages(chat.getId(), member.getId()) < max) {
            return reverseOrderMessageList(
                    messageRepository.findSliceByChat(chat, backward)
                            .map(m -> new DetailMessageDto(m))
            );
        }

        //마지막으로 읽은 메세지부터 반환
        return naturalOrderMessageList(
                messageRepository.findSliceByChatAndIdGreaterThanEqual(chat, chatMember.getLastReadMessage().getId(), forward)
                        .map(m -> new DetailMessageDto(m))
                , true);
    }

    public MessageListDto findMessagesBefore(Long chatId, Long memberId, Long before) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();
        if (!chatMemberRepository.existsByChatAndMember(chat, member)) {
            throw new NotFoundException();
        }

        Slice<Message> slice = messageRepository.findSliceByChatAndIdLessThan(chat, before, backward);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));
        return reverseOrderMessageList(result);
    }

    public MessageListDto findMessagesAfter(Long chatId, Long memberId, Long after) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();
        if (!chatMemberRepository.existsByChatAndMember(chat, member)) {
            throw new NotFoundException();
        }

        Slice<Message> slice = messageRepository.findSliceByChatAndIdGreaterThan(chat, after, forward);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));
        return naturalOrderMessageList(result, false);
    }

    public int countUnreadMessages(Long chatId, Long memberId) {

        ChatMember chatMember = chatMemberRepository.findByChatAndMember(chatId, memberId)
                .orElseThrow(IllegalArgumentException::new);
        Message last = chatMember.getLastReadMessage();


        if (last == null) {
            return messageRepository.countByCreatedTimeAfter(chatMember.getChat(), toSqlDateTime(chatMember.getCreatedTime())).intValue();
        }
        return messageRepository.countByCreatedTimeAfter(chatMember.getChat(), toSqlDateTime(last.getCreatedTime())).intValue();

    }

}
