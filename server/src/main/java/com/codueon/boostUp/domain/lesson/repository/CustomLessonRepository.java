package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.dto.Get.GetLesson;
import com.codueon.boostUp.domain.lesson.dto.Get.GetMainPageLesson;
import com.codueon.boostUp.domain.lesson.dto.Post.PostSearchLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomLessonRepository {
    Page<GetMainPageLesson> getMainPageLessons(Pageable pageable);
    Page<GetMainPageLesson> getMainPageLessonsAndBookmarkInfo(Long memberId, Pageable pageable);
    Page<GetMainPageLesson> getMainPageLessonByLanguage(Integer languageId, Pageable pageable);
    Page<GetMainPageLesson> getMainPageLessonByLanguageAndBookmarkInfo(Long memberId, Integer languageId, Pageable pageable);
    Page<GetMainPageLesson> getDetailSearchMainPageLesson(PostSearchLesson postSearchLesson, Pageable pageable);
    Page<GetMainPageLesson> getDetailSearchMainPageLessonAndGetBookmarkInfo(Long memberId, PostSearchLesson postSearchLesson, Pageable pageable);
    GetLesson getDetailLesson(Long lessonId);
    Long getMemberIdByLessonId(Long lessonId);
}
