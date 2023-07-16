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
import whyzpotato.gamjacamp.service.ChatMemberService;
import whyzpotato.gamjacamp.service.ChatService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final ChatService chatService;
    private final ChatMemberService chatMemberService;

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
        return ResponseEntity.ok(new SimpleChatDto(privateChat)); //TODO : 201 채팅 목록

    }

    @PostMapping("/group")
    public ResponseEntity<?> createPublicChat(@LoginMember SessionMember member,
                                              @Valid @RequestBody PublicChatRequest request) {

        Chat publicChat = chatService.createPublicChat(member.getId(), request.getTitle(), request.getCapacity());
        return ResponseEntity.ok(new SimpleChatDto(publicChat)); //TODO : 201 채팅 목록

    }

    @PostMapping("/{roomId}/members")
    public ResponseEntity<?> enterChat(@LoginMember SessionMember member,
                                       @PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.enter(roomId, member.getId())); //TODO : 201 채팅 메세지 내역
    }

    @PostMapping("/{roomId}/last-read/{messageId}")
    public ResponseEntity<?> updateLastReadMessage(@LoginMember SessionMember member,
                                                   @PathVariable Long roomId,
                                                   @PathVariable Long messageId) {

        chatMemberService.updateLastReadMessage(roomId, member.getId(), messageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getMassages(@LoginMember SessionMember member,
                                         @PathVariable Long roomId,
                                         @RequestParam(required = false) Long start) {

        if (start == null)
            return ResponseEntity.ok(chatService.findMessages(roomId, member.getId()));

        return ResponseEntity.ok(chatService.findMessages(roomId, member.getId(), start));

    }

}
