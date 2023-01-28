package com.codueon.boostUp.domain.reveiw.dto;

import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.querydsl.core.annotations.QueryProjection;
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
    private Integer score;
    private String comment;
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public GetReview(Long reviewId,
                     String image,
                     String name,
                     Integer score,
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
