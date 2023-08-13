package whyzpotato.gamjacamp.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PrivateChatRequest;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PublicChatRequest;
import whyzpotato.gamjacamp.controller.dto.ChatMemberDto.EnteredChat;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.DetailMessageDto;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageDto;
import whyzpotato.gamjacamp.service.ChatMemberService;
import whyzpotato.gamjacamp.service.ChatService;
import whyzpotato.gamjacamp.service.MessageService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final ChatService chatService;
    private final ChatMemberService chatMemberService;
    private final MessageService messageService;

    @GetMapping("/csrf")
    public CsrfToken csrf(HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        log.debug("csrf token : {}", csrf.getToken().toString());
        return csrf;
    }

    @PostMapping("/single")
    public ResponseEntity<?> createPrivateChat(@LoginMember SessionMember member,
                                               @Valid @RequestBody PrivateChatRequest request) {

        return ResponseEntity.ok(chatService.createPrivateChat(member.getId(), request.getTo()));
    }

    @PostMapping("/group")
    public ResponseEntity<?> createPublicChat(@LoginMember SessionMember member,
                                              @Valid @RequestBody PublicChatRequest request) {

        return ResponseEntity.ok(chatService.createPublicChat(member.getId(), request));
    }

    @PostMapping("/{roomId}/members")
    public ResponseEntity<?> enterChat(@LoginMember SessionMember member,
                                       @PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.enterChat(roomId, member.getId())); //TODO : 201 채팅 메세지 내역
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

    @GetMapping
    public ResponseEntity<Map<String, Object>> chatList(@LoginMember SessionMember member) {

        List<EnteredChat> chats = chatMemberService.enteredChatList(member.getId());
        return ResponseEntity.ok(new HashMap<>() {{
            put("chats", chats);
        }});
    }

}
