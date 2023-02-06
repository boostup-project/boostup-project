package com.codueon.boostUp.domain.lesson.dto.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
public class PostSearchLesson {
    private String name;
    private Integer address;
    private Integer language;

    @PositiveOrZero(message = "경력은 0년 이상이어야 합니다.")
    private Integer career;

    @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
    private Integer startCost;

    @Positive(message = "가격은 1원 이상이어야 합니다.")
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
