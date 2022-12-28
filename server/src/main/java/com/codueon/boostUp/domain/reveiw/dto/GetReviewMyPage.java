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
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetReviewMyPage {
    private String profileImage;
    private List<String> languages;
    private String name;
    private String title;
    private String company;
    private Integer career;
    private Integer cost;
    private List<String> address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer score;
    private String comment;
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public GetReviewMyPage(Review review,
                           Lesson lesson,
                           LocalDateTime startTime,
                           LocalDateTime endTime) {
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.languages = lesson.getLessonLanguages().stream()
                .map(language -> language.getLanguages().getLanguages())
                .collect(Collectors.toList());
        this.address = lesson.getLessonAddresses().stream()
                .map(address -> address.getAddress().getAddress())
                .collect(Collectors.toList());
        this.name = lesson.getName();
        this.title = lesson.getTitle();
        this.company = lesson.getCompany();
        this.career = lesson.getCareer();
        this.cost = lesson.getCost();
        this.startTime = startTime; // 야매
        this.endTime = endTime; // 야매
        this.score = review.getScore();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
    }
}
