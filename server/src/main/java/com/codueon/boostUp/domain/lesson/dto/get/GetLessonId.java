package com.codueon.boostUp.domain.lesson.dto.get;

import lombok.Getter;

@Getter
public class GetLessonId {
    private final Long lessonId;

    public GetLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }
}
