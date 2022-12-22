package com.codueon.boostUp.domain.lesson.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LESSON_ID")
    private Long id;
    private String title;
    private String career;
    private String introduction;
    private int cost;
    private String personality;
    private String curriculum;
    private long memberId;

    @OneToMany(mappedBy = "lession", cascade = CascadeType.ALL)
    private List<CareerImage> careerImages = new ArrayList<>();
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonLanguage> lessonLanguages = new ArrayList<>();
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonAddress> lessonAddresses = new ArrayList<>();
}
