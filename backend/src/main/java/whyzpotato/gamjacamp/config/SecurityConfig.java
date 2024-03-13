package whyzpotato.gamjacamp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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
                .cors().configurationSource(corsConfigurationSource());

        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        //allow frame-option for sockJS
//        http
//                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions
//                        .sameOrigin()))
        http
                .headers().frameOptions().disable();

        http
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/rooms/**").permitAll()
                .antMatchers("/customer/**").hasRole(Role.CUSTOMER.name())
                .antMatchers("/owner/**").hasRole(Role.OWNER.name())
                .antMatchers("/csrf/**").authenticated() //csrf token for sock js & spring security
                .antMatchers("/prototype/**").permitAll() //websocket test endpoint
                .antMatchers("/socket/**").authenticated() //websocket endpoint
                .anyRequest().permitAll(); //TODO denyAll();

        http
                .logout()
                .logoutSuccessUrl("http://localhost:7777"); // 로그아웃 성공시 "/" 주소로 이동

        http
                .oauth2Login()//oauth2 설정
                .authorizationEndpoint().baseUri("/oauth2/authorization")
                .and()
                .redirectionEndpoint().baseUri("/login/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService) // 소셜로그인 성공 후 조치
                .and()
                .defaultSuccessUrl("http://localhost:7777", true);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // json을 자바스크립트에서 처리할 수 있게 설정

//        config.addAllowedOrigin("http://localhost:7777");
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 모든 ip의 응답을 허용
        config.addAllowedHeader("*"); // 모든 header의 응답을 허용
        config.addAllowedMethod("*"); // 모든 post, put 등의 메서드에 응답을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);    // 모든 경로에 Cors설정
        return source;
    }
}
