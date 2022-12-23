package com.codueon.boostUp.domain.reveiw.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchReview {
    private Double score;
    private String comment;

    @Builder
    public PatchReview(Double score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
