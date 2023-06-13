package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageTestDto;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

//    @MessageMapping("/group-chat")  // STOMP메세지의 헤더가 "/app/group-chat"로 시작하는 메세지를 처리
//    @SendTo("/topic/group-chat") // 컨트롤러 처리 후 "/topic/group-chat" 을 처리하는 메세지 브로커에 ??? 전달
//    public String groupChat(@Payload String message){
//
//        log.debug("message : {}", message);
//
//        return "OK";
//
//    }

    @MessageMapping("/group-chat")  // STOMP메세지의 헤더가 "/app/group-chat"로 시작하는 메세지를 처리
    @SendTo("/topic/group-chat") // 컨트롤러 처리 후 "/topic/group-chat" 을 처리하는 메세지 브로커에 ??? 전달
    public String groupChat(@Payload ChatMessageDto.MessageName message){

        log.debug("name : {}", message.getName());

        return "OK";

    }

    @MessageMapping("/group-chat/{id}")  // STOMP메세지의 헤더가 "/app/group-chat"로 시작하는 메세지를 처리
    @SendTo("/topic/group-chat/{id}") // 컨트롤러 처리 후 "/topic/group-chat" 을 처리하는 메세지 브로커에 ??? 전달
    public String groupChatRoom(@Payload ChatMessageDto.MessageName message, @DestinationVariable String id){

        log.debug("name : {}", message.getName());
        log.debug("roomId : {}", id);


        return "OK";

    }


}
