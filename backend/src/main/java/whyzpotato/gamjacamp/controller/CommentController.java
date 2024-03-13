package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentDetail;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentInfo;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentSaveRequest;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentUpdateRequest;
import whyzpotato.gamjacamp.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post/general/comment/new/{postId}")
    public ResponseEntity createGeneralPostComment(@LoginMember SessionMember member,
                                                   @PathVariable("postId") Long postId,
                                                   @RequestBody CommentSaveRequest request) {
        return new ResponseEntity(new createdBodyDto(commentService.save(member.getId(), postId, request)), HttpStatus.CREATED);
    }

    @GetMapping("/post/general/comment/list/{postId}")
    public ResponseEntity<List<CommentInfo>> commentList(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.findCommentList(postId), HttpStatus.OK);
    }

    @PutMapping("/post/general/comment/update/{commentId}")
    public ResponseEntity<CommentDetail> updateGeneralPostComment(@LoginMember SessionMember member,
                                                                  @PathVariable("commentId") Long commentId,
                                                                  @RequestBody CommentUpdateRequest request) {
        return new ResponseEntity<>(commentService.update(member.getId(), commentId, request),HttpStatus.OK);
    }

    @DeleteMapping("/post/general/comment/delete/{commentId}")
    public ResponseEntity deleteGeneralPostComment(@LoginMember SessionMember member,
                                                   @PathVariable("commentId") Long commentId) {
        commentService.delete(member.getId(), commentId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    protected class createdBodyDto {
        private Long id;

        public createdBodyDto(Long id) {
            this.id = id;
        }
    }
}
