package com.codueon.boostUp.domain.reveiw.controller;


import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.reveiw.dto.*;
import com.codueon.boostUp.domain.reveiw.service.ReviewDbService;
import com.codueon.boostUp.domain.reveiw.service.ReviewService;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewDbService reviewDbService;

    /**
     * 사용자 리뷰 생성 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param suggestId 과외 신청 식별자
     * @param postReview 과외 신청 정보
     * @author mozzi327
     */
    @PostMapping("/lesson/{lesson-id}/suggest/{suggest-id}")
    public ResponseEntity<?> postReview(@PathVariable("lesson-id") Long lessonId,
                                        @PathVariable("suggest-id") Long suggestId,
                                        @RequestBody @Valid PostReview postReview,
                                        Authentication authentication) {
        reviewService.createStudentReview(AuthVO.of(authentication), lessonId, suggestId, postReview);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 과외 상세 페이지 리뷰 전체 조회 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param pageable 페이지 정보
     * @return MultiResponseDto(DetailInfoReviewList)
     * @author mozzi327
     */
    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity<GetAllDetailReviews> getDetailInfoReviews(@PathVariable("lesson-id") Long lessonId,
                                                                    Pageable pageable) {
        Page<GetReview> getReviews = reviewDbService.findAllDetailInfoReviews(lessonId, pageable);
        return ResponseEntity.ok().body(new GetAllDetailReviews(getReviews));
    }

    /**
     * 사용자 마이 페이지 리뷰 내역 조회 컨트롤러 메서드
     * @param pageable 페이지 정보
     * @return MultiResponseDto(MyPageReviewList)
     * @author mozzi327
     */
    @GetMapping
    public ResponseEntity<MultiResponseDto> getMyPageReview(Pageable pageable,
                                                            Authentication authentication) {
        Page<GetReviewMyPage> getReviews = reviewDbService.findAllMyPageReviews(AuthVO.ofMemberId(authentication), pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(getReviews));
    }

    /**
     * 사용자 리뷰 수정 컨트롤러 메서드
     * @param reviewId 리뷰 식별자
     * @param patchReview 리뷰 수정 정보
     * @author mozzi327
     */
    @PatchMapping("/{review-id}/modification")
    public ResponseEntity<?> updateReview(@PathVariable("review-id") Long reviewId,
                                          @RequestBody @Valid PatchReview patchReview,
                                          Authentication authentication) {
        reviewService.editReview(AuthVO.ofMemberId(authentication), reviewId, patchReview);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 리뷰 삭제 메서드
     * @param reviewId 리뷰 식별자
     * @author mozzi327
     */
    @DeleteMapping("/{review-id}")
    public ResponseEntity<?> deleteReview(@PathVariable("review-id") Long reviewId,
                                          Authentication authentication) {
        reviewService.removeReview(AuthVO.ofMemberId(authentication), reviewId);
        return ResponseEntity.noContent().build();
    }
}
