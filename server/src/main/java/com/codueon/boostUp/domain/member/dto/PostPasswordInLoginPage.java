package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPasswordInLoginPage {
    private String email;
    private String changePassword;
    private String emailCode;

    @Builder
    public PostPasswordInLoginPage(String email,
                                   String changePassword,
                                   String emailCode) {
        this.email = email;
        this.changePassword = changePassword;
        this.emailCode = emailCode;
    }
}
