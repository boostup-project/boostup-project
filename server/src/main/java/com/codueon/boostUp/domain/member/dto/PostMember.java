package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostMember {
    private String email;
    private String password;
    private String name;

    @Builder
    public PostMember(String email,
                      String password,
                      String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
