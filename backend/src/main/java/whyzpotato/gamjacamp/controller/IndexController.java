package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class IndexController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public SessionMember index() {
        SessionMember member = (SessionMember) httpSession.getAttribute("member");
        return member;
    }


}
