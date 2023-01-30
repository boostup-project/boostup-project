package com.codueon.boostUp.domain.lesson.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetLesson {
    private String profileImage;
    private List<String> languages;
    private String name;
    private String title;
    private String company;
    private Integer career;
    private Integer cost;
    private List<String> address;

    @Builder
    @QueryProjection
    public GetLesson(Lesson lesson,
                     String name) {
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.languages = lesson.getLanguageListAsString();
        this.name = name;
        this.title = lesson.getTitle();
        this.company = lesson.getCompany();
        this.career = lesson.getCareer();
        this.cost = lesson.getCost();
        this.address = lesson.getAddressListAsString();
    }
}
