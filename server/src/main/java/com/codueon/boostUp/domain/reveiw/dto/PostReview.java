package com.codueon.boostUp.domain.reveiw.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostReview {
    private Double score;
    private String comment;

    @Builder
    public PostReview(Double score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
