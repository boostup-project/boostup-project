package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetDetailLesson {
    private String title;
    private Integer cost;
    private String profileImage;
    private String name;
    private String company;
    private String introduction;
    private String language;
    private String career;
    private String address;
    private String personality;
    private String images;
    private String curriculum;
    private Boolean isBookmark;
    
    @Builder
    public GetDetailLesson(String title,
                           Integer cost,
                           String profileImage,
                           String name,
                           String company,
                           String introduction,
                           String language,
                           String career,
                           String address,
                           String personality,
                           String images,
                           String curriculum,
                           Boolean isBookmark) {
        this.title = title;
        this.cost = cost;
        this.profileImage = profileImage;
        this.name = name;
        this.company = company;
        this.introduction = introduction;
        this.language = language;
        this.career = career;
        this.address = address;
        this.personality = personality;
        this.images = images;
        this.curriculum = curriculum;
        this.isBookmark = isBookmark;
    }
}
