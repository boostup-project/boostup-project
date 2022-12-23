package com.codueon.boostUp.domain.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLesson {
        private String title;
        private String introduction;
        private String language;
        private String career;
        private String address;
        private String personality;
        private Integer cost;
        private String images;
        private String curriculum;

        @Builder
        public PostLesson(String title,
                    String introduction,
                    String language,
                    String career,
                    String address,
                    String personality,
                    Integer cost,
                    String images,
                    String curriculum) {
            this.title = title;
            this.introduction = introduction;
            this.language = language;
            this.career = career;
            this.address = address;
            this.personality = personality;
            this.cost = cost;
            this.images = images;
            this.curriculum = curriculum;
    }
}
