package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.LessonAddress;
import com.codueon.boostUp.domain.lesson.entity.LessonLanguage;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long>, CustomLessonRepository {
    Optional<Lesson> findByMemberId(Long memberId);
    Boolean existsByMemberId(Long memberId);
}
