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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostDetail;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSaveRequest;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSimple;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostUpdateRequest;
import whyzpotato.gamjacamp.service.AwsS3Service;
import whyzpotato.gamjacamp.service.PostService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/general")
    public ResponseEntity<Void> createGeneralPost(@LoginMember SessionMember member,
                                            @RequestPart("request") GeneralPostSaveRequest request,
                                            @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        Long postId = postService.saveGeneralPost(member.getId(), request, awsS3Service.uploadImages(multipartFiles));
        URI uri = ServletUriComponentsBuilder.fromUriString("/post/general")
                .path("/{postId}")
                .buildAndExpand(postId)
                .toUri();
        return ResponseEntity.created(uri).build();
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

    @PutMapping("/general/{postId}")
    public ResponseEntity<GeneralPostDetail> updateGeneralPost(@LoginMember SessionMember member,
                                                            @PathVariable("postId") Long postId,
                                                            @RequestPart("request") GeneralPostUpdateRequest request,
                                                            @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(postService.findGeneralPostImages(member.getId(), postId));
        return new ResponseEntity<>(postService.updateGeneralPost(member.getId(), postId, request, awsS3Service.uploadImages(multipartFiles)), HttpStatus.OK);
    }

    @DeleteMapping("/general/{postId}")
    public ResponseEntity<Void> deleteGeneralPost(@LoginMember SessionMember member,
                                            @PathVariable("postId") Long postId) {
        awsS3Service.removeImages(postService.findGeneralPostImages(member.getId(), postId));
        postService.delete(member.getId(), postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/general/search")
    public ResponseEntity<List<GeneralPostSimple>> searchGeneralPost(@RequestParam Long lastPostId,
                                                                     @RequestParam String keyword) {
        List<GeneralPostSimple> posts = postService.search(lastPostId, keyword).getContent();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
