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
public class LessonAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LESSON_ADDRESS_ID")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    private AddressInfo addressInfo;

    @Builder
    public LessonAddress(Long id, Integer addressId) {
        this.id = id;
        this.addressInfo = AddressInfo.findById(addressId);
    }

    public void addLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
