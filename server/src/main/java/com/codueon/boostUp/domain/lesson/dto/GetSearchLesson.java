package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetSearchLesson {
    private Long lessonId;
    private String language;
    private String title;
    private Integer cost;
    private String profileImage;
    private String name;
    private String career;
    private String address;
    private Boolean isBookmark;

    @Builder
    public GetSearchLesson(Long lessonId,
                           String language,
                           String title,
                           Integer cost,
                           String profileImage,
                           String name,
                           String career,
                           String address,
                           Boolean isBookmark) {
        this.lessonId = lessonId;
        this.language = language;
        this.title = title;
        this.cost = cost;
        this.profileImage = profileImage;
        this.name = name;
        this.career = career;
        this.address = address;
        this.isBookmark = isBookmark;
    }
}
