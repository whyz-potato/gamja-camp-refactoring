package whyzpotato.gamjacamp.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentInfo;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentSaveRequest;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentUpdateRequest;
import whyzpotato.gamjacamp.service.CommentService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post/general/comment/{postId}")
    public ResponseEntity<Void> createGeneralPostComment(@LoginMember SessionMember member,
                                                         @PathVariable("postId") Long postId,
                                                         @RequestBody CommentSaveRequest request) {

        commentService.save(member.getId(), postId, request);
        URI uri = ServletUriComponentsBuilder.fromUriString("/post/general")
                .path("/{postId}")
                .buildAndExpand(postId)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/post/general/comment/list/{postId}")
    public ResponseEntity<List<CommentInfo>> commentList(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.findCommentList(postId), HttpStatus.OK);
    }

    @PutMapping("/post/general/comment/{commentId}")
    public ResponseEntity<Void> updateGeneralPostComment(@LoginMember SessionMember member,
                                                         @PathVariable("commentId") Long commentId,
                                                         @RequestBody CommentUpdateRequest request) {
        commentService.update(member.getId(), commentId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/post/general/comment/{commentId}")
    public ResponseEntity<Void> deleteGeneralPostComment(@LoginMember SessionMember member,
                                                         @PathVariable("commentId") Long commentId) {
        commentService.delete(member.getId(), commentId);
        return ResponseEntity.noContent().build();
    }
}

