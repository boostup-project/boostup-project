package com.codueon.boostUp.domain.lesson.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LessonAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Lesson_ADRESS_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADRESS_ID")
    private Address adress;

    @Builder
    public LessonAddress(Long id, Lesson lesson, Address address) {
        this.id = id;
        this.lesson = lesson;
        this.adress = address;
    }

    public void addLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void addAddress(Address address) {
        this.adress = address;
    }
}
