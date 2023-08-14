package whyzpotato.gamjacamp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.service.ChatMemberService;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final ChatMemberService chatMemberService;
    private final HttpSession httpSession;


//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
////        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
////        accessor.setMessage("Illegal Subscription");
////        accessor.setSessionId(accessor.getSessionId());
////        return message;
//
//
//
//        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        if(accessor.getCommand().equals(StompCommand.SUBSCRIBE)){
//            String roomId = (String) accessor.getHeader("roomId");
//            if(roomId.equals("2")){
//                log.debug("preSend : 2번방은 구독할 수 없습니다.");
//                return null;
//            }
//            log.debug("preSend : SUBSCRIBE Room{}, session - {}", roomId, accessor.getSessionId());
//        } else{
//            log.debug("preSend : command - {}, session - {}", accessor.getCommand().toString(), accessor.getSessionId());
//        }
//        return message;
//    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String sessionId = accessor.getSessionId();
        String destination;

        switch (accessor.getCommand()) {
            case CONNECT:
                log.debug("Stomp Log : Session {} connect", sessionId);
                break;
            case DISCONNECT:
                log.debug("Stomp Log : Session {} disconnect", sessionId);
                break;
            case SUBSCRIBE:
                destination = accessor.getDestination();
                if (isIllegalDestination(destination)) {
                    log.info("Stomp Subscribe Failed : Session {} tried subscription destination {}", sessionId, destination);
                    StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                    errorAccessor.setMessage("Illegal Subscription");
                    errorAccessor.setSessionId(accessor.getSessionId());
                    return MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders());
                }
                log.debug("Stomp Log : Session {} subscribed destination {}", sessionId, destination);
                break;
            case SEND:
                destination = accessor.getDestination();
                if (isIllegalDestination(destination)) {
                    log.info("Stomp Send Failed : Session {} tried sending messaged to illegal destination {}", sessionId, destination);
                    StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                    errorAccessor.setMessage("Illegal Sending");
                    errorAccessor.setSessionId(accessor.getSessionId());
                    return MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders());
                }
                log.debug("Stomp Log : Session {} sent message to destination {}", sessionId, destination);
                break;

        }

        return message;
    }

    Boolean isIllegalDestination(String destination) {

        return false;

//        Long roomId;
//        Long memberId = ((SessionMember) httpSession.getAttribute("member")).getId();
//
//        try {
//            roomId = Long.parseLong(destination.replaceFirst("/topic/", ""));
//            return !chatMemberService.isEnteredChat(roomId, memberId);
//        } catch (Exception e) {
//            return true;
//        }

    }

}
