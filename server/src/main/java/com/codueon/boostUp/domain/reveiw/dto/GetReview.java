package com.codueon.boostUp.domain.reveiw.dto;

import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.global.util.Auditable;
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
    public GetReview(Review review,
                     String image,
                     String name) {
        this.reviewId = review.getId();
        this.image = image;
        this.name = name;
        this.score = review.getScore();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
    }
}
