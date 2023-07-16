package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import whyzpotato.gamjacamp.dto.comment.CommentSaveRequestDto;
import whyzpotato.gamjacamp.dto.comment.CommentUpdateRequestDto;
import whyzpotato.gamjacamp.service.CommentService;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/v1/post/general/comment/new/{memberId}/{postId}")
    public ResponseEntity createGeneralPostComment(@PathVariable("memberId") Long memberId,
                                                   @PathVariable("postId") Long postId,
                                                   @RequestBody CommentSaveRequestDto requestDto) {
        return new ResponseEntity(new createdBodyDto(commentService.save(memberId, postId, requestDto)), HttpStatus.CREATED);
    }

    @PostMapping("/v1/post/general/recomment/new/{memberId}/{postId}/{upperComentId}")
    public ResponseEntity createGeneralPostReComment(@PathVariable("memberId") Long memberId,
                                                     @PathVariable("postId") Long postId,
                                                     @PathVariable("upperComentId") Long upperCommentId,
                                                     @RequestBody CommentSaveRequestDto requestDto) {
        return new ResponseEntity(new createdBodyDto(commentService.saveReComment(memberId, postId, upperCommentId, requestDto)), HttpStatus.CREATED);
    }

    @PutMapping("/v1/post/general/comment/update/{memberId}/{commentId}")
    public ResponseEntity updateGeneralPostComment(@PathVariable("memberId") Long memberId,
                                                   @PathVariable("commentId") Long commentId,
                                                   @RequestBody CommentUpdateRequestDto requestDto) {
        // TODO return Dto
        commentService.update(memberId, commentId, requestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/v1/post/general/comment/delete/{memberId}/{commentId}")
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
