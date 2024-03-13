package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PrivateChatResponse;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PublicChatRequest;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PublicChatResponse;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.ChatRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    public PrivateChatResponse createPrivateChat(Long senderId, Long receiverId) {
        Member sender = memberRepository.findById(senderId).orElseThrow(NotFoundException::new);
        Member receiver = memberRepository.findById(receiverId).orElseThrow(NotFoundException::new);
        Chat privateChat = chatRepository.save(Chat.createPrivateChat(sender, receiver));
        return new PrivateChatResponse(privateChat, receiverId);
    }

    public PublicChatResponse createPublicChat(Long hostId, PublicChatRequest request) {
        Member host = memberRepository.findById(hostId).orElseThrow(NotFoundException::new);
        Post post = postRepository.findById(request.getPostId()).orElseThrow(NotFoundException::new);

        Chat publicChat = chatRepository.save(Chat.createPublicChat(host, post.getTitle(), request.getCapacity()));

        return new PublicChatResponse(publicChat, post.getId());
    }


    public Chat enterChat(Long chatId, Long memberId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(NotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        return chat.enter(member);
    }

    public boolean isHost(Long chatId, Long memberId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(NotFoundException::new);
        return chat.getHost().getId().equals(memberId);
    }

    public void removeChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }


}
