package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PatchLessonInfo {
    private List<Long> languages;
    private String name;
    private String title;
    private String company;
    private Integer career;
    private Integer cost;
    private List<Long> addresses;

    @Builder
    public PatchLessonInfo(List<Long> languages,
                           String name,
                           String title,
                           String company,
                           Integer career,
                           Integer cost,
                           List<Long> addresses) {
        this.languages = languages;
        this.name = name;
        this.title = title;
        this.company = company;
        this.career = career;
        this.cost = cost;
        this.addresses = addresses;
    }
}
