package whyzpotato.gamjacamp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import whyzpotato.gamjacamp.config.auth.LoginMemberArgumentResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    /**
     * 커스텀 생성한 LoginMemberArgumentResolver 를 스프링에서 인식할 수 있도록
     * WebMvcConfigurer 에 추가한다.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    /**
     * stomp
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*")
                .allowedOrigins("http://localhost:7777")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    /*
    @RequestParam 으로 Enum인 ReservationStatus 받기
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ReservationStatusConverter());
    }
}
