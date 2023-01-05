package com.codueon.boostUp.domain.suggest.pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Amount {

    private Integer total;
    private Integer taxFree;
    private Integer vat;
    private Integer point;
    private Integer discount;

    @Builder
    public Amount(Integer total,
                  Integer taxFree,
                  Integer vat,
                  Integer point,
                  Integer discount) {
        this.total = total;
        this.taxFree = taxFree;
        this.vat = vat;
        this.point = point;
        this.discount = discount;
    }
}
