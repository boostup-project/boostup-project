package com.codueon.boostUp.global.event.email.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostEmailCode {
    private String email;
    private String emailCode;

    @Builder
    public PostEmailCode(String email, String emailCode) {
        this.email = email;
        this.emailCode = emailCode;
    }
}
