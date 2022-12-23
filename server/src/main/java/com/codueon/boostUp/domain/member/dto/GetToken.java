package com.codueon.boostUp.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.net.http.HttpHeaders;

@Getter
@NoArgsConstructor
public class GetToken {
    private GetMemberInfo memberInfo;
    private HttpHeaders headers;

    @Builder
    public GetToken(GetMemberInfo memberInfo, HttpHeaders headers) {
        this.memberInfo = memberInfo;
        this.headers = headers;
    }
}
