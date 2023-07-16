package whyzpotato.gamjacamp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import whyzpotato.gamjacamp.config.auth.CustomOAuth2UserService;
import whyzpotato.gamjacamp.domain.member.Role;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors();

        //relax csrf for sockJS
        http
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/prototype/**", "/chats/**"));

        //allow frame-option for sockJS
        http
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions
                        .sameOrigin()));
        http
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/customer").hasRole(Role.CUSTOMER.name())
                .antMatchers("/owner").hasRole(Role.OWNER.name())
                .antMatchers("/csrf/**").authenticated() //csrf token for sock js & spring security
                .antMatchers("/prototype/**").permitAll() //websocket test endpoint
                .antMatchers("/chats/**").authenticated() //websocket endpoint
                .anyRequest().permitAll(); //TODO denyAll();

        http
                .logout().logoutSuccessUrl("/"); // 로그아웃 성공시 "/" 주소로 이동

        http
                .oauth2Login()//oauth2 설정
                .loginPage("/login") //TODO ?type= 해결
                .userInfoEndpoint()
                .userService(customOAuth2UserService); // 소셜로그인 성공 후 조치

        return http.build();
    }
}
