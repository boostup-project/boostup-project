package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class PostPasswordInLoginPage {
    @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$"
            , message = "8자 이상, 영문, 숫자, 특수문자가 포함되어야 합니다.")
    private String changePassword;
    private String emailCode;

    @Builder
    public PostPasswordInLoginPage(String email,
                                   String changePassword,
                                   String emailCode) {
        this.email = email;
        this.changePassword = changePassword;
        this.emailCode = emailCode;
    }
}
