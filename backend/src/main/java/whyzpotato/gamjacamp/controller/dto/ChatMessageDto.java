package whyzpotato.gamjacamp.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Slice;
import whyzpotato.gamjacamp.controller.dto.ChatMemberDto.SimpleChatMember;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageDto {

    @Data
    public static class MessageDto {
        String content;
    }

    @Getter
    @NoArgsConstructor
    public static class MessageListDto {

        @Accessors(fluent = true)
        @JsonProperty("hasNext")
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
        private SimpleChatMember from;

        public DetailMessageDto(Message message) {
            this.id = message.getId();
            this.content = message.getContent();
            this.time = message.getCreatedTime();
            this.from = new SimpleChatMember(message.getFrom());
        }

    }

}
