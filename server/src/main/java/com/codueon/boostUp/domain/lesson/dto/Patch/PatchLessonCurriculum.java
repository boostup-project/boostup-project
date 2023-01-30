package com.codueon.boostUp.domain.lesson.dto.Patch;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchLessonCurriculum {
    private String curriculum;

    @Builder
    public PatchLessonCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }
}
