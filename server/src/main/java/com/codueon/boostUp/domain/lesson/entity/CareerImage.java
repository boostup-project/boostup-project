package com.codueon.boostUp.domain.lesson.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAREER_IMAGE_ID")
    private Long id;
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_INFO_ID")
    private LessonInfo lessonInfo;

    @Builder
    public CareerImage(Long id,
                       String originFileName,
                       String fileName,
                       String filePath,
                       Long fileSize) {
        this.id = id;
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public void addLessonInfo(LessonInfo lessonInfo) {
        this.lessonInfo = lessonInfo;
    }


}
