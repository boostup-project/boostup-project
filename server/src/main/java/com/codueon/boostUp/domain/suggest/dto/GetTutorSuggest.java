package com.codueon.boostUp.domain.suggest.dto;

import com.codueon.boostUp.domain.suggest.entity.SuggestStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetTutorSuggest {
    private Long suggestId;
    private Long lessonId;
    private String name;
    private String days;
    private String languages;
    private String requests;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder
    @QueryProjection
    public GetTutorSuggest(Long suggestId,
                           String days,
                           String languages,
                           String requests,
                           SuggestStatus status,
                           LocalDateTime startTime,
                           LocalDateTime endTime,
                           Long lessonId,
                           String name) {
        this.suggestId = suggestId;
        this.lessonId = lessonId;
        this.name = name;
        this.days = days;
        this.languages = languages;
        this.requests = requests;
        this.status = status.getStatus();
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
