package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostLessonDetailEdit {
    private String introduction;
    private String detailCompany;
    private String detailCost;
    private String personality;
    private String detailLocation;

    private List<Long> careerImages;
    @Builder
    public PostLessonDetailEdit(String introduction,
                                String detailCompany,
                                String detailCost,
                                String personality,
                                String detailLocation,
                                List<Long> careerImages) {
        this.introduction = introduction;
        this.detailCompany = detailCompany;
        this.detailCost = detailCost;
        this.personality = personality;
        this.detailLocation = detailLocation;
        this.careerImages = careerImages;
    }
}
