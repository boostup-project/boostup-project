package com.codueon.boostUp.domain.suggest.pay;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPayHeader {

    private String adminKey;

    private String contentType;

    @Builder
    public TossPayHeader(String adminKey, String contentType) {
        this.adminKey = adminKey;
        this.contentType = contentType;
    }
}
