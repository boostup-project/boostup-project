package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostAttemptFindPassword {
    private String name;
    private String email;

    @Builder
    public PostAttemptFindPassword(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
