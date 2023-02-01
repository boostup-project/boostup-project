package com.codueon.boostUp.domain.suggest.dto;

import lombok.Getter;

@Getter
public class WrapQuantityCount {
    private Integer quantityCount;

    public WrapQuantityCount(Integer quantityCount) {
        this.quantityCount = quantityCount;
    }
}
