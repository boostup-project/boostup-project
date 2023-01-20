package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSearchLesson {
    private String name;
    private Integer address;
    private Integer career;
    private Integer language;
    private Integer startCost;
    private Integer endCost;

    @Builder
    public PostSearchLesson(String name,
                            Integer address,
                            Integer career,
                            Integer language,
                            Integer startCost,
                            Integer endCost) {
        this.name = name;
        this.address = address;
        this.career = career;
        this.language = language;
        this.startCost = startCost;
        this.endCost = endCost;
    }
}
