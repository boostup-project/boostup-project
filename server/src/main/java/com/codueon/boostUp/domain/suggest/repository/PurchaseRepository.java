package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByLessonIdAndMemberId(Long lessonId, Long memberId);
}
