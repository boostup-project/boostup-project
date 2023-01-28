package com.codueon.boostUp.domain.reveiw.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.LessonAddress;
import com.codueon.boostUp.domain.lesson.entity.LessonLanguage;
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
    private Long lessonId;
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
    public GetReviewMyPage(Long lessonId,
                           String name,
                           Integer score,
                           String comment,
                           LocalDateTime createdAt,
                           Lesson lesson,
                           LocalDateTime startTime,
                           LocalDateTime endTime
    ) {
        this.lessonId = lessonId;
        this.languages = lesson.getLanguageListAsString();
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.name = name;
        this.title = lesson.getTitle();
        this.cost = lesson.getCost();
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
