package whyzpotato.gamjacamp.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PrivateChatRequest;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PublicChatRequest;
import whyzpotato.gamjacamp.controller.dto.ChatDto.SimpleChatDto;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.service.ChatService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/csrf")
    public CsrfToken csrf(HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        log.debug("csrf token : {}", csrf.getToken().toString());
        return csrf;
    }

    @PostMapping("/single")
    public ResponseEntity<?> createPrivateChat(@LoginMember SessionMember member,
                                               @Valid @RequestBody PrivateChatRequest request) {

        Chat privateChat = chatService.createPrivateChat(member.getId(), request.getTo());
        return ResponseEntity.ok(new SimpleChatDto(privateChat));

    }

    @PostMapping("/group")
    public ResponseEntity<?> createPublicChat(@LoginMember SessionMember member,
                                              @Valid @RequestBody PublicChatRequest request) {

        Chat publicChat = chatService.createPublicChat(member.getId(), request.getTitle(), request.getCapacity());
        return ResponseEntity.ok(new SimpleChatDto(publicChat));

    }

}
