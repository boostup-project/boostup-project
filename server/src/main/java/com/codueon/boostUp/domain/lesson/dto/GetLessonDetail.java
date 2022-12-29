package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLessonDetail {
   private String introduction;
   private String company;
   private Integer career;
   private String personality;
   private String favoriteLocation;

   @Builder
    public GetLessonDetail(String introduction,
                           String company,
                           Integer career,
                           String personality,
                           String favoriteLocation) {
        this.introduction = introduction;
        this.company = company;
        this.career = career;
        this.personality = personality;
        this.favoriteLocation = favoriteLocation;
    }
}
