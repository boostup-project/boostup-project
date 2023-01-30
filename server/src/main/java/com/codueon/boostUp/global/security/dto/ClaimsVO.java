package com.codueon.boostUp.global.security.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClaimsVO implements Serializable {
    private List<String> roles;
    private Long id;
    private String name;
    private String sub;
    private long iat;
    private long exp;

    @Builder
    public ClaimsVO(List<String> roles, Long id, String name, String sub, long iat, long exp) {
        this.roles = roles;
        this.id = id;
        this.name = name;
        this.sub = sub;
        this.iat = iat;
        this.exp = exp;
    }
}
