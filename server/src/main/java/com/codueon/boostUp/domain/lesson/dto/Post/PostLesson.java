package com.codueon.boostUp.domain.lesson.dto.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostLesson {
        @NotBlank(message = "과외명은 공백이 아니어야 합니다.")
        @Length(max = 12, message = "글자 수는 최대 12자 이하여야 합니다.")
        private String title;

        @NotEmpty(message = "language 정보는 공백이 아니어야 합니다.")
        private List<Integer> languages;

        @NotBlank(message = "재직 회사는 공백이 아니어야 합니다.")
        private String company;

        @NotNull(message = "경력은 공백이 아니어야 합니다.")
        @PositiveOrZero(message = "경력은 0년 이상이어야 합니다.")
        private Integer career;

        @NotEmpty(message = "address 정보는 공백이 아니어야 합니다.")
        private List<Integer> address;

        @NotNull(message = "가격은 공백이 아니어야 합니다.")
        @Positive(message = "가격은 1원 이상이어야 합니다.")
        private Integer cost;

        @NotBlank(message = "한줄 소개는 공백이 아니어야 합니다.")
        private String introduction;

        @NotBlank(message = "재직 회사는 공백이 아니어야 합니다.")
        private String detailCompany;

        @NotBlank(message = "상세 장소는 공백이 아니어야 합니다.")
        private String detailLocation;

        @NotBlank(message = "성격은 공백이 아니어야 합니다.")
        private String personality;

        @NotBlank(message = "상세 가격은 공백이 아니어야 합니다.")
        private String  detailCost;
        private String curriculum;

        @Builder
        public PostLesson(
                          String title,
                          List<Integer> languages,
                          String company,
                          Integer career,
                          List<Integer> address,
                          Integer cost,
                          String introduction,
                          String detailCompany,
                          String detailLocation,
                          String personality,
                          String detailCost,
                          String curriculum) {
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

