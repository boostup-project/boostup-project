package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetStudentLesson {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> languages;
    private String requests;
    private LocalDateTime createdAt;
    private String status;

    @Builder
    public GetStudentLesson(String title,
                            LocalDateTime startTime,
                            LocalDateTime endTime,
                            List<String> languages,
                            String requests,
                            LocalDateTime createdAt,
                            String status) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.languages = languages;
        this.requests = requests;
        this.createdAt = createdAt;
        this.status = status;
    }
}
