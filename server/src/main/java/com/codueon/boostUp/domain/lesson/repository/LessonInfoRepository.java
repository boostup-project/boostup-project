package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.LessonInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonInfoRepository extends JpaRepository<LessonInfo, Long> {
    Optional<LessonInfo> findByLessonId(Long lessonId);
    Optional<LessonInfo> findByLessonIdAndMemberId(Long lessonId, Long memberId);
}
