package com.codueon.boostUp.domain.reveiw.repository;

import com.codueon.boostUp.domain.reveiw.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    Optional<Review> findByMemberIdAndId(Long memberId, Long reviewId);
    Optional<Review> findBySuggestId(Long suggestId);
    List<Review> removeAllByLessonId(Long lessonId);
    boolean existsByLessonIdAndMemberIdAndSuggestId(Long lessonId, Long memberId, Long suggestId);
}
