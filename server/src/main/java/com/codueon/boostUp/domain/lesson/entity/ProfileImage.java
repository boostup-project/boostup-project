package com.codueon.boostUp.domain.lesson.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {
    @Id
    @Column(name = "PROFILE_IMAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    @OneToOne
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;

    @Builder
    public ProfileImage(Long id,
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

    public static ProfileImage toEntity(MultipartFile profileImage, String filePath) {
        return ProfileImage.builder()
                .originFileName(profileImage.getOriginalFilename())
                .fileName(profileImage.getOriginalFilename())
                .filePath(filePath)
                .fileSize(profileImage.getSize())
                .build();
    }

    public void addLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
