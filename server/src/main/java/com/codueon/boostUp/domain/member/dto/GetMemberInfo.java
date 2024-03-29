package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMemberInfo {
    private String profileImage;
    private String name;
    private String address;
    private String company;

    @Builder
    public GetMemberInfo(String profileImage,
                         String name,
                         String address,
                         String company) {
        this.profileImage = profileImage;
        this.name = name;
        this.address = address;
        this.company = company;
    }
}
