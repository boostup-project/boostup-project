package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMemberEditInfo {
    private String name;
    private String memberImage;

    @Builder
    public GetMemberEditInfo(String name, String memberImage) {
        this.name = name;
        this.memberImage = memberImage;
    }
}
