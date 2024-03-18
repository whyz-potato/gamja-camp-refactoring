package whyzpotato.gamjacamp.controller;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PrivateChatRequest;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PrivateChatResponse;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PublicChatRequest;
import whyzpotato.gamjacamp.controller.dto.ChatDto.PublicChatResponse;
import whyzpotato.gamjacamp.controller.dto.ChatMemberDto.EnteredChat;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.DetailMessageDto;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageDto;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.MessageListDto;
import whyzpotato.gamjacamp.controller.dto.Utility.Choice;
import whyzpotato.gamjacamp.controller.dto.Utility.MultipleChoices;
import whyzpotato.gamjacamp.service.ChatMemberService;
import whyzpotato.gamjacamp.service.ChatService;
import whyzpotato.gamjacamp.service.MessageService;

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
    public ResponseEntity<PrivateChatResponse> createPrivateChat(@LoginMember SessionMember member,
                                                                 @Valid @RequestBody PrivateChatRequest request) {

        return ResponseEntity.ok(chatService.createPrivateChat(member.getId(), request.getTo()));
    }

    @PostMapping("/group")
    public ResponseEntity<PublicChatResponse> createPublicChat(@LoginMember SessionMember member,
                                                               @Valid @RequestBody PublicChatRequest request) {

        return ResponseEntity.ok(chatService.createPublicChat(member.getId(), request));
    }

    @PostMapping("/{roomId}/members")
    public ResponseEntity<Void> enterChat(@LoginMember SessionMember member,
                                          @PathVariable Long roomId) {
        chatService.enterChat(roomId, member.getId());
        URI uri = ServletUriComponentsBuilder.fromUriString("/chats")
                .path("/{roomId}")
                .buildAndExpand(roomId)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/{roomId}/last-read/{messageId}")
    public ResponseEntity<Void> updateLastReadMessage(@LoginMember SessionMember member,
                                                      @PathVariable Long roomId,
                                                      @PathVariable Long messageId) {

        chatMemberService.updateLastReadMessage(roomId, member.getId(), messageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<MessageListDto> getMassages(@LoginMember SessionMember member,
                                                      @PathVariable Long roomId,
                                                      @RequestParam(required = false) Long before,
                                                      @RequestParam(required = false) Long after,
                                                      @RequestParam(value = "max_unread", required = false, defaultValue = "15") int max) {

        if (before == null && after == null) {
            return ResponseEntity.ok(messageService.findMessages(roomId, member.getId(), max));
        } else if (before != null) {
            return ResponseEntity.ok(messageService.findMessagesBefore(roomId, member.getId(), before));
        }
        return ResponseEntity.ok(messageService.findMessagesAfter(roomId, member.getId(), after));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> chatList(@LoginMember SessionMember member) {

        List<EnteredChat> chats = chatMemberService.enteredChatList(member.getId());
        return ResponseEntity.ok(new HashMap<>() {{
            put("chats", chats);
        }});
    }

    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    public DetailMessageDto messageSend(@LoginMember SessionMember member, @DestinationVariable Long roomId,
                                        @Payload MessageDto message) {

        return messageService.createMessage(roomId, member.getId(), message.getContent());
    }

    @DeleteMapping("/{roomId}/members")
    public ResponseEntity<?> leaveChat(@LoginMember SessionMember member,
                                       @PathVariable Long roomId) {

        //방장인 경우 방 폭파 안내 후 리다이렉트
        if (chatService.isHost(roomId, member.getId())) {

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/chats/{roomId}")
                    .buildAndExpand(roomId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);

            MultipleChoices<Choice> body = MultipleChoices.<Choice>builder()
                    .messages("방장이 나가면 채팅방이 삭제되고 다른 이용자들도 더 이상 사용할 수 없습니다.")
                    .choices(List.of(Choice.builder().description("나가기").url(uri).build()))
                    .build();

            return new ResponseEntity(body, headers, HttpStatus.MULTIPLE_CHOICES);

        }

        //방장이 아니면 바로 나갈 수 있으며 채팅방에 영향을 끼치지 않는다.
        chatMemberService.removeChatMember(roomId, member.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> removeChat(@LoginMember SessionMember member,
                                        @PathVariable Long roomId) {

        if (!chatService.isHost(roomId, member.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        chatService.removeChat(roomId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/num-unread")
    public ResponseEntity<Map<String, Object>> countUnreadMessages(@LoginMember SessionMember member) {
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("numUnread", chatMemberService.countUnreadMessages(member.getId()));
        }});
    }
}
