package com.codueon.boostUp.domain.suggest.pay;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPayHeader {

    private String accept;

    private String adminKey;

    private String contentType;

    @Builder
    public KakaoPayHeader(String accept, String adminKey, String contentType) {
        this.accept = accept;
        this.adminKey = adminKey;
        this.contentType = contentType;
    }
}
