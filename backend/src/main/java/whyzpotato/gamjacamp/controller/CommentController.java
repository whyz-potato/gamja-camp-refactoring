package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/v1/post/general/comment/new/{memberId}/{postId}")
    public ResponseEntity createGeneralPostComment(@PathVariable("memberId") Long memberId,
                                                   @PathVariable("postId") Long postId,
                                                   @RequestBody CommentSaveRequest request) {
        return new ResponseEntity(new createdBodyDto(commentService.save(memberId, postId, request)), HttpStatus.CREATED);
    }

    @GetMapping("/v1/post/general/comment/list/{postId}")
    public ResponseEntity<List<CommentInfo>> commentList(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.findCommentList(postId), HttpStatus.OK);
    }

    @PutMapping("/v1/post/general/comment/update/{memberId}/{commentId}")
    public ResponseEntity<CommentDetail> updateGeneralPostComment(@PathVariable("memberId") Long memberId,
                                                                  @PathVariable("commentId") Long commentId,
                                                                  @RequestBody CommentUpdateRequest request) {
        return new ResponseEntity<>(commentService.update(memberId, commentId, request),HttpStatus.OK);
    }

    @DeleteMapping("/v1/post/general/comment/delete/{memberId}/{commentId}")
    public ResponseEntity deleteGeneralPostComment(@PathVariable("memberId") Long memberId,
                                                   @PathVariable("commentId") Long commentId) {
        commentService.delete(memberId, commentId);
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
