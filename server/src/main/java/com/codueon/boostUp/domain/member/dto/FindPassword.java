package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPassword {
    private String name;
    private String email;

    @Builder
    public FindPassword(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
