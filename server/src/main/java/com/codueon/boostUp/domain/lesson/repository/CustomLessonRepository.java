package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.dto.GetMainPageLesson;
import com.codueon.boostUp.domain.lesson.dto.PostSearchLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomLessonRepository {

    Page<GetMainPageLesson> getMainPageLessons(Pageable pageable);

    Page<GetMainPageLesson> getMainPageLessonsAndBookmarkInfo(Long memberId, Pageable pageable);

    Page<GetMainPageLesson> getMainPageLessonByLanguage(Long languageId, Pageable pageable);

    Page<GetMainPageLesson> getMainPageLessonByLanguageAndBookmarkInfo(Long memberId, Long languageId, Pageable pageable);

    Page<GetMainPageLesson> getDetailSearchMainPageLesson(PostSearchLesson postSearchLesson, Pageable pageable);

    Page<GetMainPageLesson> getDetailSearchMainPageLessonAndGetBookmarkInfo(Long memberId, PostSearchLesson postSearchLesson, Pageable pageable);

}
