package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchMember {
    private String name;
    private String address;
    private String company;

    @Builder
    public PatchMember(String name, String address, String company) {
        this.name = name;
        this.address = address;
        this.company = company;
    }
}
