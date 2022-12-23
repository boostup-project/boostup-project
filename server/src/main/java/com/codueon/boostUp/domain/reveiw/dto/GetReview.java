package com.codueon.boostUp.domain.reveiw.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetReview {
    private Long reviewId;
    private String image;
    private String name;
    private Double score;
    private String comment;
    private LocalDateTime createdAt;

    @Builder
    public GetReview(Long reviewId,
                     String image,
                     String name,
                     Double score,
                     String comment,
                     LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.image = image;
        this.name = name;
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
