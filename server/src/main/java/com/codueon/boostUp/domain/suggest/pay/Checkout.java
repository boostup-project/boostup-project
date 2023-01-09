package com.codueon.boostUp.domain.suggest.pay;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Checkout {

    private String url;

    @Builder
    public Checkout(String url) {
        this.url = url;
    }

}
