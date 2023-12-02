package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostDetail;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSaveRequest;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSimple;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostUpdateRequest;
import whyzpotato.gamjacamp.service.AwsS3Service;
import whyzpotato.gamjacamp.service.PostService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/general/new/{memberId}")
    public ResponseEntity createGeneralPost(@PathVariable("memberId") Long memberId,
                                            @RequestPart("request") GeneralPostSaveRequest request,
                                            @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        return new ResponseEntity(new createdBodyDto(postService.saveGeneralPost(memberId, request, awsS3Service.uploadImages(multipartFiles))), HttpStatus.CREATED);
    }

    @GetMapping("/general/{postId}")
    public ResponseEntity<GeneralPostDetail> detailGeneralPost(@PathVariable("postId") Long postId) {
        return new ResponseEntity<>(postService.findGeneralPost(postId), HttpStatus.OK);
    }

    @GetMapping("/general/list")
    public ResponseEntity<List<GeneralPostSimple>> generalPostList(@RequestParam Long lastPostId) {
        List<GeneralPostSimple> posts = postService.findGeneralPostList(lastPostId).getContent();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/general/update/{memberId}/{postId}")
    public ResponseEntity<GeneralPostDetail> updateGeneralPost(@PathVariable("memberId") Long memberId,
                                                            @PathVariable("postId") Long postId,
                                                            @RequestPart("request") GeneralPostUpdateRequest request,
                                                            @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(postService.findGeneralPostImages(memberId, postId));
        return new ResponseEntity<>(postService.updateGeneralPost(memberId, postId, request, awsS3Service.uploadImages(multipartFiles)), HttpStatus.OK);
    }

    @DeleteMapping("/general/delete/{memberId}/{postId}")
    public ResponseEntity deleteGeneralPost(@PathVariable("memberId") Long memberId,
                                            @PathVariable("postId") Long postId) {
        awsS3Service.removeImages(postService.findGeneralPostImages(memberId, postId));
        postService.delete(memberId, postId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/general/search")
    public ResponseEntity<List<GeneralPostSimple>> searchGeneralPost(@RequestParam Long lastPostId,
                                                                     @RequestParam String keyword) {
        List<GeneralPostSimple> posts = postService.search(lastPostId, keyword).getContent();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @Data
    protected class createdBodyDto {
        private Long id;

        public createdBodyDto(Long id) {
            this.id = id;
        }
    }
}
