package com.codueon.boostUp.domain.reveiw.service;

import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.lesson.dto.get.GetLessonInfoForAlarm;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.reveiw.dto.PatchReview;
import com.codueon.boostUp.domain.reveiw.dto.PostReview;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.END_OF_LESSON;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDbService reviewDbService;
    private final LessonDbService lessonDbService;
    private final SuggestDbService suggestDbService;
    private final ReviewEventService reviewEventService;

    /**
     * 사용자 리뷰 작성 메서드
     *
     * @param authInfo   사용자 인증 정보
     * @param lessonId   과외 식별자
     * @param suggestId  과외 신청 식별자
     * @param postReview 리뷰 등록 정보
     * @author mozzi327
     */
    @Transactional
    public void createStudentReview(AuthVO authInfo, Long lessonId, Long suggestId, PostReview postReview) {
        Suggest suggest = suggestDbService.ifNotExistSuggestThrowException(lessonId, suggestId, authInfo.getMemberId());

        // 예외 처리 : 과외 종료 상태가 아닐 때
        if (!suggest.getSuggestStatus().equals(END_OF_LESSON))
            throw new BusinessLogicException(ExceptionCode.NOT_PAY_SUCCESS);

//        reviewDbService.ifExistReviewThrowException(lessonId, memberId, suggest.getId());

        Review review = Review.builder()
                .comment(postReview.getComment())
                .score(postReview.getScore())
                .memberId(authInfo.getMemberId())
                .lessonId(lessonId)
                .suggestId(suggestId)
                .build();

        reviewDbService.saveReview(review);

        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(lessonId);
        reviewEventService.sendAlarmMessage(tutorInfo.getTutorId(), tutorInfo.getTitle(), authInfo.getName(), null, review, AlarmType.COMPLETED_REVIEW);
    }

    /**
     * 사용자 리뷰 수정 메서드
     *
     * @param reviewId    리뷰 식별자
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
     *
     * @param reviewId 리뷰 식별자
     * @author mozzi327
     */
    public void removeReview(Long memberId, Long reviewId) {
        Review findReview = reviewDbService.ifExistReturnReview(memberId, reviewId);
        reviewDbService.removeReview(findReview);
    }

    /**
     * 사용자 리뷰 삭제 메서드2
     *
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    public void removeReviewBySuggestId(Long suggestId) {
        Review findReview = reviewDbService.findBySuggestIdAndIfExistReturnReview(suggestId);
        reviewDbService.removeReview(findReview);
    }
}
