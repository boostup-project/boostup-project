package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository <ProfileImage,Long> {
    ProfileImage findByLessonId(Long lessonId);
}
