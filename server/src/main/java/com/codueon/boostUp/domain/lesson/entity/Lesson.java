package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.domain.lesson.dto.PostLesson;
import com.codueon.boostUp.domain.lesson.dto.PostLessonDetailEdit;
import com.codueon.boostUp.domain.lesson.dto.PostLessonInfoEdit;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LESSON_ID")
    private Long id;
    private String title;
    private Integer career;
    private String company;
    private Integer cost;
    private Long memberId;

    @JsonManagedReference
    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImage profileImage;

    @JsonManagedReference
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonLanguage> lessonLanguages = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonAddress> lessonAddresses = new ArrayList<>();

    @Builder
    public Lesson(Long id,
                  String title,
                  String company,
                  Integer career,
                  Integer cost,
                  Long memberId) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.career = career;
        this.cost = cost;
        this.memberId = memberId;
    }

    public static Lesson toEntity(PostLesson postLesson, String name, Long memberId) {
        return Lesson.builder()
                .title(postLesson.getTitle())
                .company(postLesson.getCompany())
                .career(postLesson.getCareer())
                .memberId(memberId)
                .cost(postLesson.getCost())
                .build();
    }

    public void addProfileImage(ProfileImage profileImage) {
        if(profileImage.getLesson() != this) profileImage.addLesson(this);
        this.profileImage = profileImage;
    }

    public void addLessonLanguage(LessonLanguage lessonLanguage) {
        if(lessonLanguage.getLesson() != this) lessonLanguage.addLesson(this);
        this.lessonLanguages.add(lessonLanguage);
    }

    public void addLessonAddress(LessonAddress lessonAddress) {
        if (lessonAddress.getLesson() != this) lessonAddress.addLesson(this);
        this.lessonAddresses.add(lessonAddress);
    }

    public void editLessonInfo(PostLessonInfoEdit postLessonInfoEdit) {
        this.title = postLessonInfoEdit.getTitle();
        this.company = postLessonInfoEdit.getCompany();
        this.career = postLessonInfoEdit.getCareer();
        this.cost = postLessonInfoEdit.getCost();
        this.lessonAddresses.clear();
        this.lessonLanguages.clear();
    }

    public List<String> getLanguageListAsString() {
        return this.lessonLanguages.stream()
                .map(language -> language.getLanguageInfo().getLanguages())
                .collect(Collectors.toList());
    }

    public List<String> getAddressListAsString() {
        return this.lessonAddresses.stream()
                .map(address -> address.getAddressInfo().getAddress())
                .collect(Collectors.toList());
    }
}