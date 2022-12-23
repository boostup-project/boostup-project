package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetMypageLesson {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String languages;
    private String requests;
    private LocalDateTime createdAt;
    private String status;

    @Builder
    public GetMypageLesson(String title,
                           LocalDateTime startTime,
                           LocalDateTime endTime,
                           String languages,
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
