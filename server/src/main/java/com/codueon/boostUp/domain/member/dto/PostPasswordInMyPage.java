package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPasswordInMyPage {
    private String changePassword;

    @Builder
    public PostPasswordInMyPage(String changePassword) {
        this.changePassword = changePassword;
    }
}
