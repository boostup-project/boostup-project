package com.codueon.boostUp.domain.lesson.entity;

import lombok.Getter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "LANGUAGES")
@Getter
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LANGUAGE_ID")
    private Long id;
    private String languages;
}
