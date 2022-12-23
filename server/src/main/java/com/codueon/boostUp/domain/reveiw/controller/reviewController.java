package com.codueon.boostUp.domain.reveiw.controller;


import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.PatchReview;
import com.codueon.boostUp.domain.reveiw.dto.PostReview;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class reviewController {

    @PostMapping("/review/lesson/{lesson-id}/suggest/{suggest-id}")
    public ResponseEntity<?> postReview(@PathVariable("lesson-id") Long lessonId,
                                        @PathVariable("suggest-id") Long suggestId,
                                        @RequestBody PostReview postReview) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/review/lesson/{lesson-id}")
    public ResponseEntity<?> getDetailReview(@PathVariable("lesson-id") Long lessonId,
                                             @RequestBody GetReview getReview) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/review")
    public ResponseEntity<?> getMypageReview(@RequestBody GetReview getReview) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/review/{review-id}/edit")
    public ResponseEntity<?> updateReview(@PathVariable("review-id") Long reviewId,
                                          @RequestBody PatchReview patchReview) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("review/{review-id}")
    public ResponseEntity<?> deleteReview(@PathVariable("review-id") Long reviewId) {
        return ResponseEntity.noContent().build();
    }
}
