package com.codueon.boostUp.domain.lesson.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LessonLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LESSON_LANGUAGE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LANGUAGE_ID")
    private Language languages;

    @Builder
    public LessonLanguage(Long id,
                          Language languages) {
        this.id = id;
        this.languages = languages;
    }

    public void addLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void addLanguage(Language language) {
        this.languages = language;
    }
}
