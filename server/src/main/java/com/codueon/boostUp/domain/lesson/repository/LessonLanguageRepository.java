package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.LessonLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonLanguageRepository extends JpaRepository <LessonLanguage, Long> {
    List<LessonLanguage> findAllByLessonId(Long lessonId);
    void deleteAllByLessonId(Long lessonId);
}
