package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.Review;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.controller.dto.review.ReviewDto;
import whyzpotato.gamjacamp.controller.dto.review.ReviewSaveDto;
import whyzpotato.gamjacamp.controller.dto.review.ReviewUpdateRequestDto;
import whyzpotato.gamjacamp.controller.dto.review.SimpleReviewResponseDto;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.ImageRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.ReviewRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final CampRepository campRepository;
//    private final ReservationRepository reservationRepository;

    /**
     * 리뷰 작성
     */
    public Long saveReview(Long memberId, Long campId, Long reservationId, ReviewSaveDto reviewSaveDto, List<String> fileNameList) {
        Member member = memberRepository.findById(memberId).get();
        Camp camp = campRepository.findById(campId).get();
//        Reservation reservation = reservationRepository.findByid(reservationId).get();
        //TODO resevation 상태 체크, 이미 작성한 리뷰가 있는지 확인

        Review review = reviewRepository.save(reviewSaveDto.toEntity(member));
        review.setCamp(camp);
//        review.setReservation(reservation);

        for (String fileName : fileNameList) {
            String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
            imageRepository.save(Image.builder().review(review).fileName(fileName).path(fileUrl).build());
        }
        return review.getId();
    }

    /**
     * 리뷰 조회
     */
    public ReviewDto findReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        return new ReviewDto(review);
    }

    /**
     * 리뷰 목록 조회
     */
    public Page<SimpleReviewResponseDto> findReviewList(Long lastReviewId) {
        Pageable sortedByIdDesc = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Review> reviews = reviewRepository.findByIdLessThan(lastReviewId, sortedByIdDesc);
        Page<SimpleReviewResponseDto> simpleReviewResponseDtoList = new SimpleReviewResponseDto().toDtoList(reviews);
        return  simpleReviewResponseDtoList;
    }

    /**
     * 리뷰 수정
     */
    public ReviewDto updateReview(Long memberId, Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto, List<String> fileNameList) {
        Member writer = memberRepository.findById(memberId).get();
        Review review = reviewRepository.findById(reviewId).get();
        if(review.getWriter().equals(writer)) {
            for (String fileName : fileNameList) {
                String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
                imageRepository.save(Image.builder().review(review).fileName(fileName).path(fileUrl).build());
            }
            return new ReviewDto(reviewRepository.save(review.update(reviewUpdateRequestDto)));
        }
        throw new NoSuchElementException();
    }

    /**
     * 리뷰 삭제
     */
    public void delete(Long memberId, Long reviewId) {
        Member writer = memberRepository.findById(memberId).get();
        Review review = reviewRepository.findById(reviewId).get();
        if(review.getWriter().equals(writer)) {
            reviewRepository.delete(review);
            return;
        }
        throw new NoSuchElementException();
    }
}
