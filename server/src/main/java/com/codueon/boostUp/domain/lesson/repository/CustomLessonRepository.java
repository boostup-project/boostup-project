package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.dto.GetMainPageLesson;
import com.codueon.boostUp.domain.lesson.dto.GetStudentLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomLessonRepository {

    Page<GetMainPageLesson> getMainPageLessons(Pageable pageable);
    Page<GetMainPageLesson> getMainPageLessonsAndBookmarkInfo(Long memberId, Pageable pageable);

    Page<GetStudentLesson> getMyPageLessonInfo(Long memberId, Pageable pageable);
}
