package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ReviewDto;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewDetail;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewSaveRequest;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewSimple;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewUpdateRequest;
import whyzpotato.gamjacamp.service.AwsS3Service;
import whyzpotato.gamjacamp.service.ReviewService;

import java.util.List;

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
        return new ResponseEntity(new createdBodyDto(reviewService.saveReview(member.getId(), campId, reservationId, request, awsS3Service.uploadImages(multipartFiles))), HttpStatus.CREATED);


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
    public ResponseEntity<ReviewDetail> updateReview(@LoginMember SessionMember member,
                                                     @PathVariable("reviewId") Long reviewId,
                                                     @RequestPart("request") ReviewUpdateRequest request,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(reviewService.findReviewImages(member.getId(), reviewId));
        return new ResponseEntity<>(reviewService.updateReview(member.getId(), reviewId, request, awsS3Service.uploadImages(multipartFiles)), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity deleteReview(@LoginMember SessionMember member,
                                       @PathVariable("reviewId") Long reviewId) {
        awsS3Service.removeImages(reviewService.findReviewImages(member.getId(), reviewId));
        reviewService.delete(member.getId(), reviewId);
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
