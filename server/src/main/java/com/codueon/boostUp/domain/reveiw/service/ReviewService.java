package com.codueon.boostUp.domain.reveiw.service;

import com.codueon.boostUp.domain.reveiw.dto.GetReview;
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
import static com.codueon.boostUp.domain.suggest.entity.Suggest.SuggestStatus.PAY_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SuggestDbService suggestDbService;

    public void createStudentReview(Long memberId, Long lessonId, Long suggestId, PostReview postReview) {
        Suggest suggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        // 예외 처리
//        if(!suggest.getStatus().equals(PAY_SUCCESS))
//            throw new BusinessLogicException(ExceptionCode.NOT_PAY_SUCCESS);
        ifExistReviewThrowException(lessonId, memberId);

        Review review = Review.builder()
                .comment(postReview.getComment())
                .score(postReview.getScore())
                .memberId(memberId)
                .lessonId(lessonId)
                .build();
        reviewRepository.save(review);
    }

    /**
     * 과외 상세페이지 리뷰 전체 조회
     * @param lessonId 과외 식별자
     * @param pageable 페이지 정보
     * @return Page(GetReview)
     * @author mozzi327
     */
    public Page<GetReview> findDetailInfoReviews(Long lessonId, Pageable pageable) {
        return reviewRepository.getReviewList(lessonId, pageable);
    }

    /**
     * 리뷰가 이미 작성되어 있는지 확인하는 메서드
     * @param lessonId 과외 식별자
     * @param memberId 페이지 정보
     */
    private void ifExistReviewThrowException(Long lessonId, Long memberId) {
        if(reviewRepository.findByLessonIdAndMemberId(lessonId, memberId).isPresent())
            throw new BusinessLogicException(ExceptionCode.REVIEW_ONLY_ONE);
    }
}
