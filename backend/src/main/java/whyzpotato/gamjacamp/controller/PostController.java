package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.dto.Post.GeneralPostDto;
import whyzpotato.gamjacamp.dto.Post.GeneralPostSaveRequestDto;
import whyzpotato.gamjacamp.dto.Post.GeneralPostUpdateRequestDto;
import whyzpotato.gamjacamp.dto.Post.SimpleGeneralPostResponseDto;
import whyzpotato.gamjacamp.service.AwsS3Service;
import whyzpotato.gamjacamp.service.PostService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class PostController {

    private final PostService postService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/v1/post/general/new/{memberId}")
    public ResponseEntity createGeneralPost(@PathVariable("memberId") Long memberId,
                                            @RequestPart("request") GeneralPostSaveRequestDto requestDto,
                                            @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        return new ResponseEntity(new createdBodyDto(postService.saveGeneralPost(memberId, requestDto, awsS3Service.uploadImages(multipartFiles))), HttpStatus.CREATED);
    }

    @GetMapping("/v1/post/general/{postId}")
    public ResponseEntity<GeneralPostDto> detailGeneralPost(@PathVariable("postId") Long postId) {
        return new ResponseEntity<>(postService.findGeneralPost(postId), HttpStatus.OK);
    }

    @GetMapping("/v1/post/general/list")
    public ResponseEntity<List<SimpleGeneralPostResponseDto>> generalPostList(@RequestParam Long lastPostId) {
        List<SimpleGeneralPostResponseDto> posts = postService.findGeneralPostList(lastPostId).getContent();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/v1/post/general/update/{memberId}/{postId}")
    public ResponseEntity<GeneralPostDto> updateGeneralPost(@PathVariable("memberId") Long memberId,
                                                            @PathVariable("postId") Long postId,
                                                            @RequestPart("request") GeneralPostUpdateRequestDto requestDto,
                                                            @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(postService.findGeneralPostImages(memberId, postId));
        return new ResponseEntity<>(postService.updateGeneralPost(memberId, postId, requestDto, awsS3Service.uploadImages(multipartFiles)), HttpStatus.OK);
    }

    @DeleteMapping("/v1/post/general/delete/{memberId}/{postId}")
    public ResponseEntity deleteGeneralPost(@PathVariable("memberId") Long memberId,
                                            @PathVariable("postId") Long postId) {
        awsS3Service.removeImages(postService.findGeneralPostImages(memberId, postId));
        postService.delete(memberId, postId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/v1/post/general/search")
    public ResponseEntity<List<SimpleGeneralPostResponseDto>> searchGeneralPost(@RequestParam Long lastPostId,
                                                                                @RequestParam String keyword) {
        List<SimpleGeneralPostResponseDto> posts = postService.search(lastPostId, keyword).getContent();
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
