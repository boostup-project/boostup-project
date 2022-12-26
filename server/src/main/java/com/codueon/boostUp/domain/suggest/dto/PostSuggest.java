package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSuggest {

    private String days;

    private String languages;

    private String requests;

    @Builder
    public PostSuggest(String days, String languages, String requests) {
        this.days = days;
        this.languages = languages;
        this.requests = requests;
    }
}
