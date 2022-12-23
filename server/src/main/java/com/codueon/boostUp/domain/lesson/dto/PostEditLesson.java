package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostEditLesson {
    private String title;
    private String career;
    private String introduction;
    private Integer cost;
    private String language;
    private String personality;
    private String address;
    private String images;
    private String curriculum;

    @Builder
    public PostEditLesson(String title,
                          String career,
                          String introduction,
                          Integer cost,
                          String language,
                          String personality,
                          String address,
                          String images,
                          String curriculum) {
        this.title = title;
        this.career = career;
        this.introduction = introduction;
        this.cost = cost;
        this.language = language;
        this.personality = personality;
        this.address = address;
        this.images = images;
        this.curriculum = curriculum;
    }
}
