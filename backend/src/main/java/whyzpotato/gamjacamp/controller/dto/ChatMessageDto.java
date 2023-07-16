package whyzpotato.gamjacamp.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageDto {

    @Data
    public static class MessageTestDto {
        String content;
    }

    @Getter
    @NoArgsConstructor
    public static class MessageListDto {

        private boolean hasNext;
        private int numberOfElements;
        private List<DetailMessageDto> messages;

        public MessageListDto(boolean hasNext, int numberOfElements, List<DetailMessageDto> messages) {
            this.hasNext = hasNext;
            this.numberOfElements = numberOfElements;
            this.messages = messages;
        }

        public MessageListDto(Slice<DetailMessageDto> result) {
            this.hasNext = result.hasNext();
            this.numberOfElements = result.getNumberOfElements();
            this.messages = result.getContent();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DetailMessageDto {

        private Long id;
        private String content;
        private LocalDateTime time;
        private Sender from;

        public DetailMessageDto(Message message) {
            this.id = message.getId();
            this.content = message.getContent();
            this.time = message.getCreatedTime();
            this.from = new Sender(message.getFrom());
        }

        @Getter
        @NoArgsConstructor
        private static class Sender {
            private Long id;
            private String username;

            public Sender(Member member) {
                this.id = member.getId();
                this.username = member.getUsername();
            }

        }

    }


}
