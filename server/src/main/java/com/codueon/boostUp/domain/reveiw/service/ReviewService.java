package com.codueon.boostUp.domain.reveiw.service;

import com.codueon.boostUp.domain.reveiw.dto.PatchReview;
import com.codueon.boostUp.domain.reveiw.dto.PostReview;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final SuggestDbService suggestDbService;
    private final ReviewDbService reviewDbService;

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

        reviewDbService.ifExistReviewThrowException(lessonId, memberId);

        Review review = Review.builder()
                .comment(postReview.getComment())
                .score(postReview.getScore())
                .memberId(memberId)
                .lessonId(lessonId)
                .suggestId(suggestId)
                .build();
        reviewDbService.saveReview(review);
    }

    /**
     * 사용자 리뷰 수정 메서드
     * @param reviewId 리뷰 식별자
     * @param patchReview 리뷰 수정 정보
     * @author mozzi327
     */
    public void editReview(Long memberId, Long reviewId, PatchReview patchReview) {
        Review findReview = reviewDbService.ifExistReturnReview(memberId, reviewId);
        findReview.editReview(patchReview);
        reviewDbService.saveReview(findReview);
    }

    /**
     * 사용자 리뷰 삭제 메서드
     * @param reviewId 리뷰 식별자
     * @author mozzi327
     */
    public void removeReview(Long memberId, Long reviewId) {
        Review findReview = reviewDbService.ifExistReturnReview(memberId, reviewId);
        reviewDbService.removeReview(findReview);
    }

    /**
     * 사용자 리뷰 삭제 메서드2
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    public void removeReviewBySuggestId(Long suggestId) {
        Review findReview = reviewDbService.findBySuggestIdAndIfExistReturnReview(suggestId);
        reviewDbService.removeReview(findReview);
    }
}
