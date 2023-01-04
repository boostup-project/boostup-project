package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.LessonAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonAddressRepository extends JpaRepository<LessonAddress, Long> {
}
