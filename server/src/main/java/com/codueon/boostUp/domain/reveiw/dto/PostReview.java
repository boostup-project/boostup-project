package com.codueon.boostUp.domain.reveiw.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostReview {
    private Integer score;
    private String comment;

    @Builder
    public PostReview(Integer score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
