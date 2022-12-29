package com.codueon.boostUp.domain.lesson.entity;

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
    private Long lessonId;
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_INFO_ID")
    private LessonInfo lessonInfo;

    @Builder
    public CareerImage(Long id,
                       Long lessonId,
                       String originFileName,
                       String fileName,
                       String filePath,
                       Long fileSize) {
        this.id = id;
        this.lessonId = lessonId;
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public void addLessonInfo(LessonInfo lessonInfo) {
        this.lessonInfo = lessonInfo;
    }
}
