package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLessonDetail {
   private String introduction;
   private String detailCompany;
   private String personality;
   private Integer detailCost;
   private String detailLocation;

   @Builder
    public GetLessonDetail(String introduction,
                           String detailCompany,
                           String personality,
                           Integer detailCost,
                           String detailLocation) {
        this.introduction = introduction;
        this.detailCompany = detailCompany;
        this.personality = personality;
        this.detailCost = detailCost;
        this.detailLocation = detailLocation;
    }
}
