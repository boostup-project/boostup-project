package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLessonDetailEdit {
    private String introduction;
    private String detailCompany;
    private String detailCost;
    private String personality;
    private String detailLoacation;

    @Builder
    public PostLessonDetailEdit(String introduction,
                                String detailCompany,
                                String detailCost,
                                String personality,
                                String detailLoacation) {
        this.introduction = introduction;
        this.detailCompany = detailCompany;
        this.detailCost = detailCost;
        this.personality = personality;
        this.detailLoacation = detailLoacation;
    }
}
