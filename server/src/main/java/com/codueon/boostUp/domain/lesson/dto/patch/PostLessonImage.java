package com.codueon.boostUp.domain.lesson.dto.patch;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLessonImage {
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public PostLessonImage(String originFileName, String fileName, String filePath, Long fileSize) {
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
