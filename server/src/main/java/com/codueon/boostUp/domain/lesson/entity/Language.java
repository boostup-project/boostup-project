package com.codueon.boostUp.domain.lesson.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "LANGUAGES")
@Getter
@NoArgsConstructor
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LANGUAGE_ID")
    private Long id;
    private String languages;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonLanguage> lessonLanguages = new ArrayList<>();

    @Builder
    public Language(Long id, String languages) {
        this.id = id;
        this.languages = languages;
    }
}
