package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long>, CustomLessonRepository {
}
