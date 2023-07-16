package whyzpotato.gamjacamp.controller.dto;

import lombok.Data;
import whyzpotato.gamjacamp.domain.chat.Chat;

import javax.validation.constraints.NotNull;

public class ChatDto {

    @Data
    public static class PrivateChatRequest {
        @NotNull
        private Long to;
    }

    @Data
    public static class PublicChatRequest {
        @NotNull
        private String title;
        @NotNull
        private int capacity;
    }

    public static class SimpleChatDto {
        private Long roomId;
        private String title;
        private int capacity;

        public SimpleChatDto(Chat chat) {
            this.roomId = chat.getId();
            this.title = chat.getTitle();
            this.capacity = chat.getCapacity();
        }
    }

}
