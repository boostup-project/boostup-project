package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long>, CustomLessonRepository {
    Optional<Lesson> findByMemberId(Long memberId);
    Optional<Lesson> findByIdAndMemberId(Long lessonId, Long memberId);
    Boolean existsByMemberId(Long memberId);

}
