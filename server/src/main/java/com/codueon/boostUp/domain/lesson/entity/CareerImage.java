package com.codueon.boostUp.domain.lesson.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAREER_IMAGE_ID")
    private long id;
    private String originFileName;
    private String fileName;
    private String filePath;
    private String fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;
}
