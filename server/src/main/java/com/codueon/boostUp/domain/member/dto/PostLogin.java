package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLogin {
    private String email;
    private String password;

    @Builder
    public PostLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
