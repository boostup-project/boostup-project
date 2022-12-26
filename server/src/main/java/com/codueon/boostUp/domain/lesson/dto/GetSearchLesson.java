package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetSearchLesson {
    private Long lessonId;
    private List<Long> languages;
    private String title;
    private Integer cost;
    private String profileImage;
    private String name;
    private String career;
    private List<Long> address;
    private Boolean isBookmark;

    @Builder
    public GetSearchLesson(Long lessonId,
                           List<Long> languages,
                           String title,
                           Integer cost,
                           String profileImage,
                           String name,
                           String career,
                           List<Long> address,
                           Boolean isBookmark) {
        this.lessonId = lessonId;
        this.languages = languages;
        this.title = title;
        this.cost = cost;
        this.profileImage = profileImage;
        this.name = name;
        this.career = career;
        this.address = address;
        this.isBookmark = isBookmark;
    }
}
