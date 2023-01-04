package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthDto {
    private String name;
    private String email;

    @Builder
    public AuthDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
