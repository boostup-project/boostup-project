package com.codueon.boostUp.domain.lesson.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CareerImageVO {
    private Long careerImageId;
    private String filePath;

    @Builder
    public CareerImageVO(Long careerImageId, String filePath) {
        this.careerImageId = careerImageId;
        this.filePath = filePath;
    }
}

