package com.codueon.boostUp.domain.reveiw.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PatchReview {
    @NotNull(message = "별점은 공백이 아니어야 합니다.")
    @Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5점 이하여야 합니다.")
    private Integer score;

    @NotBlank(message = "리뷰는 공백이 아니어야 합니다.")
    private String comment;

    @Builder
    public PatchReview(Integer score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
