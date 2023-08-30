package whyzpotato.gamjacamp.controller.dto;

import lombok.Data;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.DetailMessageDto;
import whyzpotato.gamjacamp.domain.chat.ChatMember;

import java.util.List;

public class ChatMemberDto {

    @Data
    public static class SimpleChatMember {

        private Long id;
        private String picture;
        private String username;

        public SimpleChatMember(ChatMember chatMember) {
            this.id = chatMember.getMember().getId();
            this.picture = chatMember.getMember().getPicture();
            this.username = chatMember.getUsername();
        }
    }

    @Data
    public static class EnteredChat {
        private Long roomId;
        private String title;
        private int nUnreadMessages;
        private DetailMessageDto lastMessage = null;
        private int nParticipants;

        public EnteredChat(ChatMember chatMember, Long count) {
            this.roomId = chatMember.getChat().getId();
            this.title = chatMember.getTitle();
            this.nUnreadMessages = count.intValue();
            if(chatMember.getLastReadMessage()!=null)
                this.lastMessage = new DetailMessageDto(chatMember.getLastReadMessage());
            this.nParticipants = chatMember.getChat().getChatMemberList().size();
        }
    }

}
