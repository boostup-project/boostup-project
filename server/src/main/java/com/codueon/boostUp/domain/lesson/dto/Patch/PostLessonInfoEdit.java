package com.codueon.boostUp.domain.lesson.dto.Patch;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostLessonInfoEdit {
    private List<Integer> languages;
    private String name;
    private String title;
    private String company;
    private Integer career;
    private Integer cost;
    private List<Integer> addresses;

    @Builder
    public PostLessonInfoEdit(List<Integer> languages,
                              String name,
                              String title,
                              String company,
                              Integer career,
                              Integer cost,
                              List<Integer> addresses) {
        this.languages = languages;
        this.name = name;
        this.title = title;
        this.company = company;
        this.career = career;
        this.cost = cost;
        this.addresses = addresses;
    }
}
