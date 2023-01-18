package com.codueon.boostUp.domain.suggest.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanceledAmount {
    private Integer total;
    private Integer vat;
}
