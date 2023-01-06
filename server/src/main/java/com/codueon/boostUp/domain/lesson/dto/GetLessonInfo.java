package com.codueon.boostUp.domain.lesson.dto;

import com.codueon.boostUp.domain.lesson.entity.LessonInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLessonInfo {
   private String introduction;
   private String detailCompany;
   private String personality;
   private String detailCost;
   private String detailLocation;

   @Builder
    public GetLessonInfo(LessonInfo lessonInfo) {
        this.introduction = lessonInfo.getIntroduction();
        this.detailCompany = lessonInfo.getCompanies();
        this.personality = lessonInfo.getPersonality();
        this.detailCost = lessonInfo.getCosts();
        this.detailLocation = lessonInfo.getFavoriteLocation();
    }
}
