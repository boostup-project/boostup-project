package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostSuggest {
    @NotBlank(message = "희망 요일은 공백이 아니어야 합니다.")
    private String days;

    @NotBlank(message = "희망 언어는 공백이 아니어야 합니다.")
    private String languages;

    @NotBlank(message = "요청 사항은 공백이 아니어야 합니다.")
    private String requests;

    @Builder
    public PostSuggest(String days, String languages, String requests) {
        this.days = days;
        this.languages = languages;
        this.requests = requests;
    }
}
