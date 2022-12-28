package com.codueon.boostUp.domain.reveiw.repository;

import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.GetReviewMyPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomReviewRepository {
    Page<GetReview> getReviewList(Long lessonId, Pageable pageable);
    Page<GetReviewMyPage> getMyPageReviewList(Long memberId, Pageable pageable);
}
