package whyzpotato.gamjacamp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.controller.dto.ChatMemberDto.SimpleChatMember;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.ChatType;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class ChatDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrivateChatRequest {
        @NotNull
        private Long to;
    }

    @Getter
    @NoArgsConstructor
    public static class PrivateChatResponse {
        private Long roomId;
        private Long to;
        private List<SimpleChatMember> participants;
        private String title;
        private int nParticipants;
        private int capacity;
        private ChatType type;

        public PrivateChatResponse(Chat chat, Long to) {
            this.roomId = chat.getId();
            this.to = to;
            this.participants = chat.getChatMemberList()
                    .stream().map(m -> new SimpleChatMember(m))
                    .collect(Collectors.toList());
            this.title = chat.getTitle();
            this.nParticipants = chat.getChatMemberList().size();
            this.capacity = chat.getCapacity();
            this.type = chat.getType();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicChatRequest {
        @NotNull
        private Long postId;
        @NotNull
        private int capacity;
    }

    @Getter
    @NoArgsConstructor
    public static class PublicChatResponse {
        private Long roomId;
        private Long postId;
        private List<SimpleChatMember> participants;
        private String title;
        private int nParticipants;
        private int capacity;
        private ChatType type;

        public PublicChatResponse(Chat chat, Long postId) {
            this.roomId = chat.getId();
            this.postId = postId;
            this.participants = chat.getChatMemberList()
                    .stream().map(m -> new SimpleChatMember(m))
                    .collect(Collectors.toList());
            this.title = chat.getTitle();
            this.nParticipants = chat.getChatMemberList().size();
            this.capacity = chat.getCapacity();
            this.type = chat.getType();
        }

    }


}
