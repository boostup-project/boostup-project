package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostLessonImage {
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    public static class Response {
        private String filePath;
//        @Builder
//        public Response(LessonImage lessonImage)
    }
}
