package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostFindPassword {
    private String name;
    private String email;

    @Builder
    public PostFindPassword(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
