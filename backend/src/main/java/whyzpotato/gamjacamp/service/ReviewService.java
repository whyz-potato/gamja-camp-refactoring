package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewDetail;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewSaveRequest;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewSimple;
import whyzpotato.gamjacamp.controller.dto.ReviewDto.ReviewUpdateRequest;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Review;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.*;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final CampRepository campRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 리뷰 작성
     */
    public Long saveReview(Long memberId, Long campId, Long reservationId, ReviewSaveRequest request, List<String> fileNameList) {
        Member member = memberRepository.findById(memberId).get();
        Camp camp = campRepository.findById(campId).get();
        Reservation reservation = reservationRepository.findById(reservationId).get();

        if (reviewRepository.findByReservation(reservation).isPresent()) {
            throw new IllegalStateException("이미 작성한 리뷰가 존재합니다.");
        }

        Review review = reviewRepository.save(request.toEntity(member, camp, reservation));
        review.setCamp(camp);
        review.setReservation(reservation);

        for (String fileName : fileNameList) {
            String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
            imageRepository.save(Image.builder().review(review).fileName(fileName).path(fileUrl).build());
        }

        campRepository.save(camp.updateRate(reviewRepository.getRateAverage(camp)));

        return review.getId();
    }

    /**
     * 리뷰 조회
     */
    public ReviewDetail findReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        return new ReviewDetail(review);
    }

    /**
     * 리뷰 목록 조회
     */
    public Page<ReviewSimple> findReviewList(Long lastReviewId) {
        Pageable sortedByIdDesc = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Review> reviews = reviewRepository.findByIdLessThan(lastReviewId, sortedByIdDesc);
        Page<ReviewSimple> reviewSimpleList = new ReviewSimple().toList(reviews);
        return  reviewSimpleList;
    }

    /**
     * 개인 리뷰 목록 조회
     */
    public Page<ReviewSimple> findMyReviewList(Long memberId, Long lastReviewId) {
        Member writer = memberRepository.findById(memberId).get();
        Pageable sortedByIdDesc = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Review> reviews = reviewRepository.findByWriterAndIdLessThan(writer, lastReviewId, sortedByIdDesc);
        Page<ReviewSimple> myReviewSimpleList = new ReviewSimple().toList(reviews);
        return myReviewSimpleList;
    }

    /**
     * 리뷰 수정
     */
    public ReviewDetail updateReview(Long memberId, Long reviewId, ReviewUpdateRequest request, List<String> fileNameList) {
        Member writer = memberRepository.findById(memberId).get();
        Review review = reviewRepository.findById(reviewId).get();
        Camp camp = campRepository.findById(review.getCamp().getId()).get();

        if(review.getWriter().equals(writer)) {
            review.getImages().clear();
            if(fileNameList != null) {
                for (String fileName : fileNameList) {
                    String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
                    review.getImages().add(imageRepository.save(Image.builder().review(review).fileName(fileName).path(fileUrl).build()));
                }
            }
            ReviewDetail reviewDetail = new ReviewDetail(reviewRepository.save(review.update(request)));
            campRepository.save(camp.updateRate(reviewRepository.getRateAverage(camp)));
            return reviewDetail;
        }
        throw new NoSuchElementException();
    }

    /**
     * 리뷰 삭제
     */
    public void delete(Long memberId, Long reviewId) {
        Member writer = memberRepository.findById(memberId).get();
        Review review = reviewRepository.findById(reviewId).get();
        Camp camp = campRepository.findById(review.getCamp().getId()).get();

        if(review.getWriter().equals(writer)) {
            reviewRepository.delete(review);
            campRepository.save(camp.updateRate(reviewRepository.getRateAverage(camp)));
            return;
        }
        throw new NoSuchElementException();
    }

    public List<Image> findReviewImages(Long memberId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        Member member = memberRepository.findById(memberId).get();
        if(review.getWriter().equals(member)) {
            return review.getImages();
        }
        throw new NoSuchElementException();
    }
}
