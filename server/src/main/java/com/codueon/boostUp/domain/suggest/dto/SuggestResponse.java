package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SuggestResponse {

    private Long suggestId;

    private String title;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String language;

    private String requests;

    private String status;

    private LocalDateTime createdAt;

    @Builder
    public SuggestResponse(Long suggestId,
                           String title,
                           String name,
                           LocalDateTime startTime,
                           LocalDateTime endTime,
                           String language,
                           String requests,
                           String status,
                           LocalDateTime createdAt) {
        this.suggestId = suggestId;
        this.title = title;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
        this.requests = requests;
        this.status = status;
        this.createdAt = createdAt;
    }
}
