package whyzpotato.gamjacamp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import whyzpotato.gamjacamp.config.auth.LoginMemberArgumentResolver;

import java.util.List;

/**
 * 커스텀 생성한 LoginMemberArgumentResolver 를 스프링에서 인식할 수 있도록
 * WebMvcConfigurer 에 추가한다.
 */

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

}
