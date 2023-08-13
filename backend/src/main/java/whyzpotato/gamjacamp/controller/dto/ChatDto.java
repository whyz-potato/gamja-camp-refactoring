package whyzpotato.gamjacamp.controller.dto;

import lombok.Data;
import whyzpotato.gamjacamp.controller.dto.ChatMemberDto.SimpleChatMember;
import whyzpotato.gamjacamp.domain.chat.Chat;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class ChatDto {

    @Data
    public static class PrivateChatRequest {
        @NotNull
        private Long to;
    }

    @Data
    public static class PrivateChatResponse {
        private Long roomId;
        private Long to;
        private List<SimpleChatMember> participants;
        private String title;
        private int nParticipants;
        private int capacity;

        public PrivateChatResponse(Chat chat, Long to) {
            this.roomId = chat.getId();
            this.to = to;
            this.participants = chat.getChatMemberList()
                    .stream().map(m -> new SimpleChatMember(m))
                    .collect(Collectors.toList());
            this.title = chat.getTitle();
            this.nParticipants = chat.getChatMemberList().size();
            this.capacity = chat.getCapacity();
        }
    }

    @Data
    public static class PublicChatRequest {
        @NotNull
        private Long postId;
        @NotNull
        private int capacity;
    }

    @Data
    public static class PublicChatResponse {
        private Long roomId;
        private Long postId;
        private List<SimpleChatMember> participants;
        private String title;
        private int nParticipants;
        private int capacity;

        public PublicChatResponse(Chat chat, Long postId) {
            this.roomId = chat.getId();
            this.postId = postId;
            this.participants = chat.getChatMemberList()
                    .stream().map(m -> new SimpleChatMember(m))
                    .collect(Collectors.toList());
            this.title = chat.getTitle();
            this.nParticipants = chat.getChatMemberList().size();
            this.capacity = chat.getCapacity();
        }

    }


}
