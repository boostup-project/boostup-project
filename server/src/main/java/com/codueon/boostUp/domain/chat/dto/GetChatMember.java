package com.codueon.boostUp.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetChatMember {
    private Long memberId;
    private String profileImage;
    private String name;

    @Builder
    public GetChatMember(Long memberId, String profileImage, String name) {
        this.memberId = memberId;
        this.profileImage = profileImage;
        this.name = name;
    }
}
