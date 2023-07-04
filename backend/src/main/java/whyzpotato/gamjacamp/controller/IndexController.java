package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
public class IndexController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public SessionMember index(@LoginMember SessionMember member) {
        log.debug("Session {} login", httpSession.getId());
        return member;
    }


}
