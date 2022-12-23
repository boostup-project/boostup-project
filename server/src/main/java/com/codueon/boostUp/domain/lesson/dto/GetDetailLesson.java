package com.codueon.boostUp.domain.lesson.dto;

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

}
