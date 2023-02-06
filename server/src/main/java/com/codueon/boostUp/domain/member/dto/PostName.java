package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class PostName {
    @NotBlank(message = "이름은 공백이 아니어야 합니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$" , message = "특수문자는 사용할 수 없습니다.")
    @Length(min = 5, max = 12, message = "이름은 5자 이상 12자 이하여햐 합니다.")
    private String name;

    @Builder
    public PostName(String name) {
        this.name = name;
    }
}
