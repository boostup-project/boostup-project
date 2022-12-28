package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostEditLesson {
    private String title;
    private String career;
    private String introduction;
    private Integer cost;
    private List<Long> languages;
    private String personality;
    private String address;
    private String images;
    private String curriculum;

    @Builder
    public PostEditLesson(String title,
                          String career,
                          String introduction,
                          Integer cost,
                          List<Long> languages,
                          String personality,
                          String address,
                          String images,
                          String curriculum) {
        this.title = title;
        this.career = career;
        this.introduction = introduction;
        this.cost = cost;
        this.languages = languages;
        this.personality = personality;
        this.address = address;
        this.images = images;
        this.curriculum = curriculum;
    }
}
