package whyzpotato.gamjacamp.controller.dto;

import lombok.Data;

public class ChatMessageDto {

    @Data
    public static class MessageTestDto{
        Long id;
        String message;
    }

    @Data
    public static class MessageName{
        String name;
    }

}
