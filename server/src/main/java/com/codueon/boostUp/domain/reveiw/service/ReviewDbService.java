package com.codueon.boostUp.domain.reveiw.service;

import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.GetReviewMyPage;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.reveiw.repository.ReviewRepository;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewDbService {
    private final ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * 사용자 리뷰 정보 조회 메서드
     * @param reviewId 리뷰 식별자
     * @return Review
     * @author mozzi327
     */
    public Review ifExistReturnReview(Long memberId, Long reviewId) {
        return reviewRepository.findByMemberIdAndId(memberId, reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
    }

    /**
     * 사용자 리뷰 정보 조회 메서드2
     * @param suggestId 신청 식별자
     * @return Review
     * @author LeeGoh
     */
    public Review findBySuggestIdAndIfExistReturnReview(Long suggestId) {
        return reviewRepository.findBySuggestId(suggestId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
    }

    /**
     * 사용자 리뷰가 이미 작성되어 있는지 확인하는 메서드
     * @param lessonId 과외 식별자
     * @param memberId 페이지 정보
     * @author mozzi327
     */
    public void ifExistReviewThrowException(Long lessonId, Long memberId, Long suggestId) {
        if(reviewRepository.existsByLessonIdAndMemberIdAndSuggestId(lessonId, memberId, suggestId))
            throw new BusinessLogicException(ExceptionCode.REVIEW_ONLY_ONE);
    }

    /**
     * 과외 상세페이지 리뷰 전체 조회 메서드
     * @param lessonId 과외 식별자
     * @param pageable 페이지 정보
     * @return Page(GetReview)
     * @author mozzi327
     */
    @Transactional
    public Page<GetReview> findAllDetailInfoReviews(Long lessonId, Pageable pageable) {
        return reviewRepository.getReviewList(lessonId, pageable);
    }

    /**
     * 사용자 마이페이지 리뷰 전체 조회 메서드
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page(GetReviewMyPage)
     * @author mozzi327
     */
    @Transactional
    public Page<GetReviewMyPage> findAllMyPageReviews(Long memberId, Pageable pageable) {
        return reviewRepository.getMyPageReviewList(memberId, pageable);
    }

    /**
     * 리뷰 삭제 메서드
     * @param review 리뷰 정보
     * @author LeeGoh
     */
    public void removeReview(Review review) {
        reviewRepository.delete(review);
    }

    /**
     * 리뷰 전체 삭제 메서드
     * @param lessonId 과외 식별자
     * @author Quartz614
     */
    public void removeAllByReviews(Long lessonId) {
        reviewRepository.removeAllByLessonId(lessonId);
    }
}
