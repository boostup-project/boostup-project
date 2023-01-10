package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPassword {
    private String password;

    @Builder
    public PostPassword(String password) {
        this.password = password;
    }
}
