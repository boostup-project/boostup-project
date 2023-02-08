package com.codueon.boostUp.domain.lesson.dto.utils;

import lombok.Getter;

@Getter
public class WrapLessonId {
    private Long wrapLessonId;

    public WrapLessonId(Long wrapLessonUrl) {
        this.wrapLessonId = wrapLessonUrl;
    }
}
