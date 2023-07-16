package whyzpotato.gamjacamp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {


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
    public Message<?> preSend(Message<?> message, MessageChannel channel){

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String sessionId = accessor.getSessionId();

        switch (accessor.getCommand()) {
            case CONNECT:
                log.debug("Stomp Log : Session {} connect", sessionId);
                break;
            case DISCONNECT:
                log.debug("Stomp Log : Session {} disconnect", sessionId);
                break;
            case SUBSCRIBE:
                String destination = accessor.getDestination();
                if(isIllegalDestination(destination)){
                    log.debug("Stomp Log : Session {} subscribe destination {} fail", sessionId, destination);
                    StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                    errorAccessor.setMessage("Illegal Subscription");
                    errorAccessor.setSessionId(accessor.getSessionId());
                    return MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders());
                }
                log.debug("Stomp Log : Session {} subscribe destination {} success", sessionId, destination);
                break;
            case SEND:
                break;

        }

        return message;
    }

    Boolean isIllegalDestination(String destination){

        Long roomId;
        try{
             roomId = Long.parseLong(destination.replaceFirst("/topic/", ""));
             //TODO db의 입장 내역 확인
        }
        catch (Exception e){
            return true;
        }
        return (roomId != 1L);

    }

//    @Override
//    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        String sessionId = accessor.getSessionId();
//
//        log.debug("postSend : command - {}, session - {}", accessor.getCommand().toString(), accessor.getSessionId());
//
//        switch (accessor.getCommand()) {
//            case CONNECT:
//                break;
//            case DISCONNECT:
//                log.debug("Disconnect Session {}", sessionId);
//                break;
//
//        }
//    }


}
