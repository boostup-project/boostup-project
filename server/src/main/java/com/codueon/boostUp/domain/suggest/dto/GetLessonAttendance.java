package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLessonAttendance {
    private Integer quantity;
    private Integer quantityCount;
    private Integer progress;

    @Builder
    public GetLessonAttendance(Integer quantity,
                               Integer quantityCount,
                               Integer progress) {
        this.quantity = quantity;
        this.quantityCount = quantityCount;
        this.progress = progress;
    }
}
