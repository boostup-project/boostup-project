package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostChangePassword {
    private String email;
    private String password;

    @Builder
    public PostChangePassword(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
