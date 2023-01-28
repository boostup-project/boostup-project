package com.codueon.boostUp.domain.reveiw.service;

import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.GetReviewMyPage;
import com.codueon.boostUp.domain.reveiw.dto.PatchReview;
import com.codueon.boostUp.domain.reveiw.dto.PostReview;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.reveiw.repository.ReviewRepository;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SuggestDbService suggestDbService;

    /**
     * 사용자 리뷰 작성 메서드
     * @param memberId 사용자 식별자
     * @param lessonId 과외 식별자
     * @param suggestId 과외 신청 식별자
     * @param postReview 리뷰 등록 정보
     * @author mozzi327
     */
    @Transactional
    public void createStudentReview(Long memberId, Long lessonId, Long suggestId, PostReview postReview) {
        Suggest suggest = suggestDbService.ifNotExistSuggestThrowException(lessonId, suggestId, memberId);

        // 예외 처리 : 과외 종료 상태가 아닐 때
//        if(!suggest.getStatus().equals(END_OF_LESSON))
//            throw new BusinessLogicException(ExceptionCode.NOT_PAY_SUCCESS);

        // 리뷰 내역이 이미 존재할 때 예외 처리
        ifExistReviewThrowException(lessonId, memberId);

        Review review = Review.builder()
                .comment(postReview.getComment())
                .score(postReview.getScore())
                .memberId(memberId)
                .lessonId(lessonId)
                .suggestId(suggestId)
                .build();
        reviewRepository.save(review);
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
     * 사용자 리뷰 수정 메서드
     * @param reviewId 리뷰 식별자
     * @param patchReview 리뷰 수정 정보
     * @author mozzi327
     */
    public void editReview(Long memberId, Long reviewId, PatchReview patchReview) {
        Review findReview = ifExistReturnReview(memberId, reviewId);
        findReview.editReview(patchReview);
        reviewRepository.save(findReview);
    }

    /**
     * 사용자 리뷰 삭제 메서드
     * @param reviewId 리뷰 식별자
     * @author mozzi327
     */
    public void removeReview(Long memberId, Long reviewId) {
        Review findReview = ifExistReturnReview(memberId, reviewId);
        reviewRepository.delete(findReview);
    }

    /**
     * 리뷰 전체 삭제 메서드
     *
     * @param lessonId 과외 식별자
     * @author Quartz614
     */
    public void removeAllByReviews(Long lessonId) {
        reviewRepository.removeAllByLessonId(lessonId);
    }

    /**
     * 사용자 리뷰가 이미 작성되어 있는지 확인하는 메서드
     * @param lessonId 과외 식별자
     * @param memberId 페이지 정보
     * @author mozzi327
     */
    private void ifExistReviewThrowException(Long lessonId, Long memberId) {
        if(reviewRepository.findByLessonIdAndMemberId(lessonId, memberId).isPresent())
            throw new BusinessLogicException(ExceptionCode.REVIEW_ONLY_ONE);
    }

    /**
     * 사용자 리뷰 정보 조회 메서드
     * @param reviewId 리뷰 식별자
     * @return Review
     * @author mozzi327
     */
    private Review ifExistReturnReview(Long memberId, Long reviewId) {
        return reviewRepository.findByMemberIdAndId(memberId, reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
    }
}
