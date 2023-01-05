package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.domain.lesson.dto.PostLesson;
import lombok.*;

import javax.persistence.*;
import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LESSON_ID")
    private Long id;
    private String name;
    private String title;
    private Integer career;
    private String company;
    private Integer cost;
    private Long memberId;

    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL)
    private ProfileImage profileImage;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonLanguage> lessonLanguages = new ArrayList<>();
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonAddress> lessonAddresses = new ArrayList<>();

    @Builder
    public Lesson(Long id,
                  String name,
                  String title,
                  String company,
                  Integer career,
                  Integer cost,
                  Long memberId) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.company = company;
        this.career = career;
        this.cost = cost;
        this.memberId = memberId;
    }

    public static Lesson toEntity(PostLesson postLesson, String name, Long memberId) {
        return Lesson.builder()
                .name(name)
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
}