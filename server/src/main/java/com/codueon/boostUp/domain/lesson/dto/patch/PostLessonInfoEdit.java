package com.codueon.boostUp.domain.lesson.dto.patch;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostLessonInfoEdit {
    private List<Integer> languages;

    @Length(max = 12, message = "글자 수는 최대 12자 이하여야 합니다.")
    private String title;
    private String company;

    @PositiveOrZero(message = "경력은 0년 이상이어야 합니다.")
    private Integer career;

    @Positive(message = "가격은 1원 이상이어야 합니다.")
    private Integer cost;
    private List<Integer> addresses;
    private String editState;

    @Builder
    public PostLessonInfoEdit(List<Integer> languages,
                              String title,
                              String company,
                              Integer career,
                              Integer cost,
                              List<Integer> addresses,
                              String editState) {
        this.languages = languages;
        this.title = title;
        this.company = company;
        this.career = career;
        this.cost = cost;
        this.addresses = addresses;
        this.editState = editState;
    }
}
