package com.codueon.boostUp.domain.lesson.dto.get;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLessonInfoForAlarm {
    private Long tutorId;
    private String tutorName;
    private String title;

    @Builder
    @QueryProjection
    public GetLessonInfoForAlarm(Long tutorId, String tutorName, String title) {
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.title = title;
    }
}
