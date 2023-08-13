package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PrivateChatResponse;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PublicChatRequest;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PublicChatResponse;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.DetailMessageDto;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageListDto;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.*;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final PostRepository postRepository;


    public PrivateChatResponse createPrivateChat(Long senderId, Long receiverId) {
        Member sender = memberRepository.findById(senderId).get();
        Member receiver = memberRepository.findById(receiverId).get();
        Chat privateChat = chatRepository.save(Chat.createPrivateChat(sender, receiver));
        return new PrivateChatResponse(privateChat, receiverId);
    }

    public PublicChatResponse createPublicChat(Long hostId, PublicChatRequest request) {
        Member host = memberRepository.findById(hostId).get();
        Post post = postRepository.findById(request.getPostId()).get();

        Chat publicChat = chatRepository.save(Chat.createPublicChat(host, post.getTitle(), request.getCapacity()));

        return new PublicChatResponse(publicChat, post.getId());
    }


    public Chat enterChat(Long chatId, Long memberId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();
        return chat.enter(member);
    }

    public MessageListDto findMessages(Long chatId, Long memberId) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException());
        Member member = memberRepository.findById(memberId).get();

        if (!chatMemberRepository.existsByChatAndMember(chat, member)) {
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

        if (!chatMemberRepository.existsByChatAndMember(chat, member)) {
            throw new NotFoundException();
        }

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "message_id"));
        Slice<Message> slice = messageRepository.findSliceByChatAndIdLessThan(chat, start, pageRequest);
        Slice<DetailMessageDto> result = slice.map(m -> new DetailMessageDto(m));

        return new MessageListDto(result);
    }


}
