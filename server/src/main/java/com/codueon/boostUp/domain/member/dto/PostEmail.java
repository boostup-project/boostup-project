package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostEmail {
    private String email;

    @Builder
    public PostEmail(String email) {
        this.email = email;
    }
}
