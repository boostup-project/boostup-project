package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.global.file.UploadFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public ProfileImage(String originFileName,
                       String fileName,
                       String filePath,
                       Long fileSize) {
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
}
