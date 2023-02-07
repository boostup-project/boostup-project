package com.codueon.boostUp.domain.lesson.dto.get;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetTutorLesson {
    private Long lessonId;
    private String profileImage;
    private List<String> languages;
    private String name;
    private String title;
    private String company;
    private Integer career;
    private Integer cost;
    private List<String> address;

    @Builder
    public GetTutorLesson(Lesson lesson,
                          Member member) {
        this.lessonId = lesson.getId();
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.languages = lesson.getLanguageListAsString();
        this.name = member.getName();
        this.title = lesson.getTitle();
        this.company = lesson.getCompany();
        this.career = lesson.getCareer();
        this.cost = lesson.getCost();
        this.address = lesson.getAddressListAsString();
    }
}
