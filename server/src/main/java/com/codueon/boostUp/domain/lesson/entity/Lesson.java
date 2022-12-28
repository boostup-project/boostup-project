package com.codueon.boostUp.domain.lesson.entity;

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
    private String title;
    private Integer career;
    private String introduction;
    private Integer cost;
    private String personality;
    private String curriculum;
    private Long memberId;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<CareerImage> careerImages = new ArrayList<>();
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonLanguage> lessonLanguages = new ArrayList<>();
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonAddress> lessonAddresses = new ArrayList<>();

    @Builder
    public Lesson(Long id,
                  String title,
                  Integer career,
                  String introduction,
                  Integer cost,
                  String personality,
                  String curriculum,
                  Long memberId) {
        this.id = id;
        this.title = title;
        this.career = career;
        this.introduction = introduction;
        this.cost = cost;
        this.personality = personality;
        this.curriculum = curriculum;
        this.memberId = memberId;
    }
}