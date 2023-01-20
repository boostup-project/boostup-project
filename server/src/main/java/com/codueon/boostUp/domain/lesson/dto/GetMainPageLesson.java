package com.codueon.boostUp.domain.lesson.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetMainPageLesson {
    private Long lessonId;
    private List<String> languages;
    private String title;
    private Integer cost;
    private String profileImage;
    private String name;
    private String company;
    private Integer career;
    private List<String> address;
    private boolean bookmark;

    @Builder
    @QueryProjection
    public GetMainPageLesson(Lesson lesson,
                             boolean bookmark) {
        this.lessonId = lesson.getId();
        this.languages = lesson.getLanguageListAsString();
        this.title = lesson.getTitle();
        this.cost = lesson.getCost();
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.name = lesson.getName();
        this.company = lesson.getCompany();
        this.career = lesson.getCareer();
        this.address = lesson.getAddressListAsString();
        this.bookmark = bookmark;
    }
}
