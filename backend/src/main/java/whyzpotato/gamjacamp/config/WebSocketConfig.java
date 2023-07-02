package whyzpotato.gamjacamp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("스톰프 엔드포인트 설정");
        registry.addEndpoint("/prototype") // WebSocket handshake endpoint (HTTP URL)
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

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().permitAll()
                .simpDestMatchers("/app/**").permitAll()
                .simpSubscribeDestMatchers("/topic/**").permitAll()
                .anyMessage().denyAll(); //endpoint

//        messages
//                .nullDestMatcher().permitAll() // CONNECT, HEARTBEAT 등은 열어준다.
//                .simpDestMatchers("/app/**").authenticated()
//                .simpSubscribeDestMatchers("/topic/**").authenticated()
//                .anyMessage().denyAll();
    }

    @Override
    protected void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
