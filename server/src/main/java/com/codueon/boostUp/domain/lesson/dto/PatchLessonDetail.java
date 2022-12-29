package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchLessonDetail {
    private String introduction;
    private String company;
    private Integer career;
    private String personality;
    private String favoriteLoacation;

    @Builder
    public PatchLessonDetail(String introduction,
                             String company,
                             Integer career,
                             String personality,
                             String favoriteLoacation) {
        this.introduction = introduction;
        this.company = company;
        this.career = career;
        this.personality = personality;
        this.favoriteLoacation = favoriteLoacation;
    }
}
