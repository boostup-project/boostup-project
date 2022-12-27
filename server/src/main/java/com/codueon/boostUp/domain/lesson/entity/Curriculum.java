package com.codueon.boostUp.domain.lesson.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum {

    @Id
    @Column(name = "CURRICULUM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(max = 5000)
    private String curriculum;
    private Long lessonId;

    @Builder
    public Curriculum(Long id,
                      String curriculum,
                      Long lessonId) {
        this.id = id;
        this.curriculum = curriculum;
        this.lessonId = lessonId;
    }
}
