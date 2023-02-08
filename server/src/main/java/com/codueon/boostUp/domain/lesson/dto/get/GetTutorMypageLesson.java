package com.codueon.boostUp.domain.lesson.dto.get;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetTutorMypageLesson {
    private List<String> languages;
    private String name;
    private String title;
    private String company;
    private Integer career;
    private Integer cost;
    private List<String> addresses;

    @Builder
    public GetTutorMypageLesson(List<String> languages,
                                String name,
                                String title,
                                String company,
                                Integer career,
                                Integer cost,
                                List<String> addresses) {
        this.languages = languages;
        this.name = name;
        this.title = title;
        this.company = company;
        this.career = career;
        this.cost = cost;
        this.addresses = addresses;
    }
}
