package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageTestDto;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CustomStompChatController {

    /**
     * @param message : 전송할 메세지, JSON 객체 뿐 아니라 문자열도 가능하다.
     * @return 메세지 브로커에 메세지 전달
     * @MessageMapping("/group-chat-string") : STOMP 메세지의 헤더가 "/app/group-chat-string"로 시작하는 메세지를 처리한다
     * @SendTo("/topic/group-chat-string") : 컨트롤러 처리 후 "/topic/group-chat-string" 을 처리하는 메세지 브로커에 전달
     */
    @MessageMapping("/group-chat-string")
    @SendTo("/topic/group-chat-string")
    public String groupChat(@Payload String message) {
        log.debug("message : {}", message);
        return message;
    }

    /**
     * @param message : 전송할 메세지, JSON 객체
     * @return 메세지 브로커에 메세지 수정 후 전달
     * @MessageMapping("/group-chat") : STOMP 메세지의 헤더가 "/app/group-chat"로 시작하는 메세지를 처리한다
     * @SendTo("/topic/group-chat-string") : 컨트롤러 처리 후 "/topic/group-chat" 을 처리하는 메세지 브로커에 메세지 전달
     */
    @MessageMapping("/group-chat")  // STOMP메세지의 헤더가 "/app/group-chat"로 시작하는 메세지를 처리
    @SendTo("/topic/group-chat") // 컨트롤러 처리 후 "/topic/group-chat" 을 처리하는 메세지 브로커에 ??? 전달
    public MessageTestDto groupChat(@Payload MessageTestDto message) {
        String content = message.getContent();
        log.debug("message : {}", content);
        message.setContent("???: " + content);
        return message;
    }

    @MessageMapping("/group-chat/{roomId}")  // STOMP메세지의 헤더가 "/app/group-chat"로 시작하는 메세지를 처리
    @SendTo("/topic/group-chat/{roomId}") // 컨트롤러 처리 후 "/topic/group-chat" 을 처리하는 메세지 브로커에 ??? 전달
    public MessageTestDto groupChatRoom(@Payload MessageTestDto message, @DestinationVariable String roomId) {

        log.debug("payload : {}", message.toString());
        log.debug("roomId : {}", roomId);

        String content = message.getContent();
        log.debug("message : {}", content);
        message.setContent("???: " + content + " from room " + roomId);
        return message;

    }

    @GetMapping("/csrf")
    public @ResponseBody CsrfToken csrf(HttpServletRequest request){
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        log.debug("csrf token : {}", csrf.getToken().toString());
        return csrf;
    }


}
