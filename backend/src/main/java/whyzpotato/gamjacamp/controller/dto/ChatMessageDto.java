package whyzpotato.gamjacamp.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Slice;
import whyzpotato.gamjacamp.controller.dto.ChatMemberDto.SimpleChatMember;
import whyzpotato.gamjacamp.domain.chat.Message;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
        private Boolean isStacked = false;

        protected MessageListDto(boolean hasNext, int numberOfElements, List<DetailMessageDto> messages, boolean isStacked) {
            this.hasNext = hasNext;
            this.numberOfElements = numberOfElements;
            this.messages = messages;
            this.isStacked = isStacked;
        }

        public static MessageListDto naturalOrderMessageList(Slice<DetailMessageDto> result, boolean isStacked){
            return new MessageListDto(result.hasNext(), result.getNumberOfElements(), result.getContent(), isStacked);
        }

        public static MessageListDto reverseOrderMessageList(Slice<DetailMessageDto> result){
            ArrayList<DetailMessageDto> messages = new ArrayList<>(result.getContent());
            Collections.reverse(messages);
            return new MessageListDto(result.hasNext(), result.getNumberOfElements(), messages, false);
        }



    }

    @Getter
    @NoArgsConstructor
    public static class DetailMessageDto {

        private Long id;
        private String content;
        private LocalDate date;
        private LocalTime time;
        private SimpleChatMember from;

        public DetailMessageDto(Message message) {
            this.id = message.getId();
            this.content = message.getContent();
            this.date = message.getCreatedTime().toLocalDate();
            this.time = message.getCreatedTime().toLocalTime();
            this.from = new SimpleChatMember(message.getFrom());
        }
    }

}
