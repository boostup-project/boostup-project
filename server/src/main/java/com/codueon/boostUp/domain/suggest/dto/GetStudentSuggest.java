package com.codueon.boostUp.domain.suggest.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetStudentSuggest {

    private Long suggestId;

    private String name;

    private String title;

    private String profileImage;

    private String company;

    private Integer career;

    private Integer cost;

    private List<String> languages;

    private List<String> address;

    private String status;

    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public GetStudentSuggest(Suggest suggest,
                             Lesson lesson) {
        this.suggestId = suggest.getId();
        this.name = lesson.getName();
        this.title = lesson.getTitle();
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.company = lesson.getCompany();
        this.career = lesson.getCareer();
        this.cost = lesson.getCost();
//        this.languages = languages;
//        this.address = address;
        this.languages = lesson.getLessonLanguages().stream()
                .map(language -> language.getLanguages().getLanguages())
                .collect(Collectors.toList());
        this.address = lesson.getLessonAddresses().stream()
                .map(address -> address.getAdress().getAddress())
                .collect(Collectors.toList());
        this.status = suggest.getStatus().getStatus();
        this.createdAt = LocalDateTime.now();
    }
}
