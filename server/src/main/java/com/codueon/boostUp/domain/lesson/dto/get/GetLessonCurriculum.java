package com.codueon.boostUp.domain.lesson.dto.get;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLessonCurriculum {
    private String curriculum;

    @Builder
    public GetLessonCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }
}
