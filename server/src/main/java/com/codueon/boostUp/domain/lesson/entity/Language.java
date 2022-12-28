package com.codueon.boostUp.domain.lesson.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "LANGUAGES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LANGUAGE_ID")
    private Long id;
    private String languages;

    @Builder
    public Language(Long id, String languages) {
        this.id = id;
        this.languages = languages;
    }
}
