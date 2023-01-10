package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostName {
    private String name;

    @Builder
    public PostName(String name) {
        this.name = name;
    }
}
