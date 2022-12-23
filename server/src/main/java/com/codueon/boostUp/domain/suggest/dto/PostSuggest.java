package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostSuggest {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String language;

    private String requests;

    @Builder
    public PostSuggest(LocalDateTime startTime, LocalDateTime endTime, String language, String requests) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
        this.requests = requests;
    }
}
