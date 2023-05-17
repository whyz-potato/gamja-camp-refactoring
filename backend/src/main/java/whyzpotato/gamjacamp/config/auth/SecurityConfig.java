package whyzpotato.gamjacamp.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable();
        http
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**").permitAll()
                .anyRequest().authenticated();

        http
                .logout().logoutSuccessUrl("/"); // 로그아웃 성공시 "/" 주소로 이동
        http
                .oauth2Login()//oauth2 설정
                .userInfoEndpoint()
                    .userService(customOAuth2UserService); // 소셜로그인 성공 후 조치

        return http.build();
    }
}
