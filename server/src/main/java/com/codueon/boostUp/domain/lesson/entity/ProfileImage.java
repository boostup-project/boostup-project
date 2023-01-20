package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.global.file.UploadFile;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
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

    public static ProfileImage toEntity(UploadFile uploadFile, String filePath) {
        return ProfileImage.builder()
                .originFileName(uploadFile.getOriginFileName())
                .fileName(uploadFile.getFileName())
                .filePath(filePath)
                .fileSize(uploadFile.getFileSize())
                .build();
    }

    public void addLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
