package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class PostAttemptFindPassword {

    @NotBlank(message = "이름은 공백이 아니어야 합니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$" , message = "특수문자는 사용할 수 없습니다.")
    @Length(min = 5, max = 12, message = "이름은 5자 이상 12자 이하여햐 합니다.")
    private String name;

    @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @Builder
    public PostAttemptFindPassword(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
