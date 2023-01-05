package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostLesson {
        private Long lessonId;
        private String title;
        private List<Long> languages;
        private String company;
        private Integer career;
        private List<Long> address;
        private Integer cost;
        private String introduction;
        private String detailCompany;
        private String detailLocation;
        private String personality;
        private String  detailCost;
        private String curriculum;

        @Builder
        public PostLesson(Long lessonId,
                          String title,
                          List<Long> languages,
                          String company,
                          Integer career,
                          List<Long> address,
                          Integer cost,
                          String introduction,
                          String detailCompany,
                          String detailLocation,
                          String personality,
                          String detailCost,
                          String curriculum) {
                this.lessonId = lessonId;
                this.title = title;
                this.languages = languages;
                this.company = company;
                this.career = career;
                this.address = address;
                this.cost = cost;
                this.introduction = introduction;
                this.detailCompany = detailCompany;
                this.detailLocation = detailLocation;
                this.personality = personality;
                this.detailCost = detailCost;
                this.curriculum = curriculum;
        }
}

