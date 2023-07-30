package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:7777", allowedHeaders = "*")
public class LoginController {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private HashMap<String, String> paramRole = new HashMap<>() {{
        put("C", "CUSTOMER");
        put("c", "CUSTOMER");
        put("O", "OWNER");
        put("o", "OWNER");
    }};

    @GetMapping("/login")
    public List<String> login(HttpSession httpSession, @RequestParam(required = false, defaultValue = "c") String type) {

        log.debug("login controller");


        //회원 구분 값이 잘못된 경우
        String role = (String) Optional.ofNullable(paramRole.get(type))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 인자"));

        //회원 타입을 세션에 저장하여 회원가입시 role 구분이 가능하게 한다.
        httpSession.setAttribute("type", role);

        //TODO
        //return oauth provider urls
        return new ArrayList<String>(List.of("/oauth2/authorization/google", "/oauth2/authorization/naver", "/oauth2/authorization/kakao"));
    }

}
