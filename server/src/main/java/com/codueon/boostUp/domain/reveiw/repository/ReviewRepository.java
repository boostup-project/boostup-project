package com.codueon.boostUp.domain.reveiw.repository;

import com.codueon.boostUp.domain.reveiw.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    Optional<Review> findByLessonIdAndMemberId(Long lessonId, Long memberId);
}
