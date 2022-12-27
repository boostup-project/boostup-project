package com.codueon.boostUp.domain.reveiw.controller;


import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.PatchReview;
import com.codueon.boostUp.domain.reveiw.dto.PostReview;
import com.codueon.boostUp.domain.reveiw.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class reviewController {
    private ReviewService reviewService;

    /**
     * 학생 리뷰 생성 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param suggestId 과외 신청 식별자
     * @param postReview 과외 신청 정보
     * @author mozzi327
     */
    @PostMapping("/lesson/{lesson-id}/suggest/{suggest-id}")
    public ResponseEntity<?> postReview(@PathVariable("lesson-id") Long lessonId,
                                        @PathVariable("suggest-id") Long suggestId,
                                        @RequestBody PostReview postReview) {
        Long memberId = 1L; // 임시 하드 코딩 ㅎㅎ
        reviewService.createStudentReview(memberId, lessonId, suggestId, postReview);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 과외 상세 페이지 리뷰 전체 조회 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param pageable 페이지 정보
     * @return MultiResponseDto
     * @author mozzi327
     */
    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity<?> getDetailInfoReviews(@PathVariable("lesson-id") Long lessonId,
                                                  Pageable pageable) {
        Page<GetReview> getReviewList = reviewService.findDetailInfoReviews(lessonId, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(getReviewList));
    }

    @GetMapping
    public ResponseEntity<?> getMypageReview() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{review-id}/edit")
    public ResponseEntity<?> updateReview(@PathVariable("review-id") Long reviewId,
                                          @RequestBody PatchReview patchReview) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{review-id}")
    public ResponseEntity<?> deleteReview(@PathVariable("review-id") Long reviewId) {
        return ResponseEntity.noContent().build();
    }
}
