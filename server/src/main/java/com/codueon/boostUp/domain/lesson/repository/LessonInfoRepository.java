package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.LessonInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonInfoRepository extends JpaRepository<LessonInfo, Long> {
}
