package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurriculumRepository extends JpaRepository <Curriculum, Long> {
    Optional<Curriculum> findByLessonId(Long lessonId);
    Optional<Curriculum> findByLessonIdAndMemberId(Long lessonId, Long memberId);
    void deleteByLessonId(Long lessonId);
}
