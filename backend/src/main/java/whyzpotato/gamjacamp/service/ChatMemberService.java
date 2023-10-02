package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ChatMemberDto.EnteredChat;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.ChatMemberRepository;
import whyzpotato.gamjacamp.repository.ChatRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.MessageRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss.SSSSSS");


    public void updateLastReadMessage(Long chatId, Long memberId, Long messageId) {

        Member member = memberRepository.findById(memberId).get();
        Chat chat = chatRepository.findById(chatId).orElseThrow(NotFoundException::new);
        Message message = messageRepository.findById(messageId).orElseThrow(NotFoundException::new);

        ChatMember chatMember = chatMemberRepository.findByChatAndMember(chat, member).get();
        chatMember.updateLastReadMessage(message);

    }

    public boolean isEnteredChat(Long chatId, Long memberId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(NotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        return chatMemberRepository.existsByChatAndMember(chat, member);
    }

    public boolean isEnteredChat(Chat chat, Member member) {
        return chatMemberRepository.existsByChatAndMember(chat, member);
    }

    public List<EnteredChat> enteredChatList(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);

        List<EnteredChat> chats = chatMemberRepository.findByMemberOrderByChatLastModified(member)
                .stream()
                .map(cm -> {
                    LocalDateTime latest;
                    if (cm.getLastReadMessage() != null)
                        latest = cm.getLastReadMessage().getCreatedTime();
                    else
                        latest = cm.getCreatedTime();

                    LocalDateTime formattedLatest = LocalDateTime.parse(latest.plusNanos(500).format(formatter), formatter);
                    return new EnteredChat(cm, messageRepository.countByCreatedTimeAfter(cm.getChat(), formattedLatest));
                })
                .collect(Collectors.toList());
        return chats;
    }

    public void removeChatMember(Long chatId, Long memberId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(NotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);

        if (isEnteredChat(chat, member)) {
            ChatMember chatMember = chatMemberRepository.findByChatAndMember(chat, member).get();
            chat.getChatMemberList().remove(chatMember);
            chatMemberRepository.delete(chatMember);
        }
    }


    public int countUnreadMessages(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);

        return chatMemberRepository.findByMember(member)
                .stream()
                .mapToInt(cm -> {
                    LocalDateTime latest;
                    if (cm.getLastReadMessage() != null)
                        latest = cm.getLastReadMessage().getCreatedTime();
                    else
                        latest = cm.getCreatedTime();

                    LocalDateTime formattedLatest = LocalDateTime.parse(latest.plusNanos(500).format(formatter), formatter);
                    log.debug("latest : {}", latest);
                    log.debug("time : {}", formattedLatest);

                    return messageRepository.countByCreatedTimeAfter(cm.getChat(), formattedLatest).intValue();
                }).sum();

    }


}
