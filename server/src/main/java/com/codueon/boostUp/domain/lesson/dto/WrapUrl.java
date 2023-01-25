package com.codueon.boostUp.domain.lesson.dto;

import lombok.Getter;

@Getter
public class WrapUrl {
    private String lessonUrl;

    public WrapUrl(String lessonUrl) {
        this.lessonUrl = lessonUrl;
    }
}
