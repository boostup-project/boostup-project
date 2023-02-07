package com.codueon.boostUp.domain.suggest.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.suggest.entity.SuggestStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetStudentSuggest {
    private Long suggestId;
    private Long lessonId;
    private String name;
    private String title;
    private String profileImage;
    private String company;
    private Integer career;
    private Integer cost;
    private List<String> languages;
    private List<String> address;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean reviewCheck;

    @Builder
    @QueryProjection
    public GetStudentSuggest(Long suggestId,
                             String name,
                             SuggestStatus status,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             Lesson lesson,
                             boolean reviewCheck) {
        this.suggestId = suggestId;
        this.lessonId = lesson.getId();
        this.name = name;
        this.title = lesson.getTitle();
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.company = lesson.getCompany();
        this.career = lesson.getCareer();
        this.cost = lesson.getCost();
        this.languages = lesson.getLanguageListAsString();
        this.address = lesson.getAddressListAsString();
        this.status = status.getStatus();
        this.startTime = startTime;
        this.endTime = endTime;
        this.reviewCheck = reviewCheck;
    }
}
