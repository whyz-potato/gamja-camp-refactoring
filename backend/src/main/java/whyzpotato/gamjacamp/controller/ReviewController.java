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
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewDetail;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewSaveRequest;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewSimple;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewUpdateRequest;
import whyzpotato.gamjacamp.service.AwsS3Service;
import whyzpotato.gamjacamp.service.ReviewService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/{campId}/{reservationId}")
    public ResponseEntity createReview(@LoginMember SessionMember member,
                                       @PathVariable("campId") Long campId,
                                       @PathVariable("reservationId") Long reservationId,
                                       @RequestPart("request") ReviewSaveRequest request,
                                       @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        Long reviewId = reviewService.saveReview(member.getId(), campId, reservationId, request,
                awsS3Service.uploadImages(multipartFiles));
        URI uri = ServletUriComponentsBuilder.fromUriString("/review")
                .path("/{reviewId}")
                .buildAndExpand(reviewId)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetail> detailReview(@PathVariable("reviewId") Long reviewId) {
        return new ResponseEntity<>(reviewService.findReview(reviewId), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReviewSimple>> ReviewList(@RequestParam Long lastReviewId) {
        List<ReviewSimple> reviews = reviewService.findReviewList(lastReviewId).getContent();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/list/my")
    public ResponseEntity<List<ReviewSimple>> MyReviewList(@LoginMember SessionMember member,
                                                           @RequestParam Long lastReviewId) {
        List<ReviewSimple> reviews = reviewService.findMyReviewList(member.getId(), lastReviewId).getContent();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@LoginMember SessionMember member,
                                             @PathVariable("reviewId") Long reviewId,
                                             @RequestPart("request") ReviewUpdateRequest request,
                                             @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(reviewService.findReviewImages(member.getId(), reviewId));
        reviewService.updateReview(member.getId(), reviewId, request,
                awsS3Service.uploadImages(multipartFiles));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@LoginMember SessionMember member,
                                             @PathVariable("reviewId") Long reviewId) {
        awsS3Service.removeImages(reviewService.findReviewImages(member.getId(), reviewId));
        reviewService.delete(member.getId(), reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
