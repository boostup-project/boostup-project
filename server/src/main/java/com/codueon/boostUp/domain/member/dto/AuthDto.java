package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthDto {
    private Long memberId;
    private String memberImage;
    private String name;
    private String email;
    private boolean lessonExistence;

    @Builder
    public AuthDto(Long memberId,
                   String memberImage,
                   String name,
                   String email,
                   boolean lessonExistence) {
        this.memberId = memberId;
        this.memberImage = memberImage;
        this.name = name;
        this.email = email;
        this.lessonExistence = lessonExistence;
    }
}
