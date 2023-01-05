package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchMember {
    private String name;

    @Builder
    public PatchMember(String name) {
        this.name = name;
    }
}
