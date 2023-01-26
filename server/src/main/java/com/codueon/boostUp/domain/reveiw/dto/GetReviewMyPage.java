package com.codueon.boostUp.domain.reveiw.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetReviewMyPage {
    private String profileImage;
    private List<String> languages;
    private String name;
    private String title;
    private Integer cost;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer score;
    private String comment;
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public GetReviewMyPage(Review review,
                           Lesson lesson,
                           String tutorName,
                           LocalDateTime startTime,
                           LocalDateTime endTime) {
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.languages = lesson.getLanguageListAsString();
        this.name = tutorName;
        this.title = lesson.getTitle();
        this.cost = lesson.getCost();
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = review.getScore();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
    }
}
