package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostEmail {
    @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @Builder
    public PostEmail(String email) {
        this.email = email;
    }
}
