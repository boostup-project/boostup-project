package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPasswordDto {
    private String name;
    private String email;

    @Builder
    public FindPasswordDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
