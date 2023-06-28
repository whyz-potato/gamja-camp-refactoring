package whyzpotato.gamjacamp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.debug("preSend : command - {}, session - {}", accessor.getCommand().toString(), accessor.getSessionId());


        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String sessionId = accessor.getSessionId();

        log.debug("postSend : command - {}, session - {}", accessor.getCommand().toString(), accessor.getSessionId());

        switch (accessor.getCommand()) {
            case CONNECT:
                break;
            case DISCONNECT:
                log.debug("Disconnect Session {}", sessionId);
                break;

        }


    }
}
