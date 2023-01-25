package com.codueon.boostUp.domain.suggest.dto;

import lombok.Getter;

@Getter
public class GetQuantityCount {
    private Integer quantityCount;

    public GetQuantityCount(Integer quantityCount) {
        this.quantityCount = quantityCount;
    }
}
