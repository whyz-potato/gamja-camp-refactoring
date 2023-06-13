package whyzpotato.gamjacamp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
//public class WebSocketBrokerConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer{
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("스톰프 엔드포인트 설정");
        registry.addEndpoint("/test") // WebSocket handshake endpoint (HTTP URL)
                .setAllowedOriginPatterns("*")
                .withSockJS(); //SockJS 라이브러리 확장
        ;

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("메세지 브로커 설정");
        registry.enableSimpleBroker("/queue", "/topic"); // STOMP 메세지의 destination header가 /topic, /queue 로 시작하면 SimpleBroker(기본 내장 브로커)가 바로 처리한다.
        registry.setApplicationDestinationPrefixes("/app"); // STOMP 메세지의 destination header가 "/app" 로 시작하면 @MessageMapping 이 달린 컨트롤러로 전달된다.
    }

//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpDestMatchers("/test/socket-endpoint").permitAll();
//    }


}
