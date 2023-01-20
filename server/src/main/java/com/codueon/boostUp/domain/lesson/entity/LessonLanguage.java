package com.codueon.boostUp.domain.lesson.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    private LanguageInfo languageInfo;

    @Builder
    public LessonLanguage(Long id,
                          Integer languageId) {
        this.id = id;
        this.languageInfo = LanguageInfo.findById(languageId);
    }

    public void addLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
