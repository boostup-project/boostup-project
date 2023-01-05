package com.codueon.boostUp.domain.suggest.pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CardInfo {

    private String purchaseCorp;
    private String purchaseCorpCode;
    private String issuerCorp;
    private String issuerCorpCode;
    private String bin;
    private String cardType;
    private String installMonth;
    private String approvedId;
    private String cardMid;
    private String interestFreeInstall;
    private String cardItemCode;

    @Builder
    public CardInfo(String purchaseCorp,
                    String purchaseCorpCode,
                    String issuerCorp,
                    String issuerCorpCode,
                    String bin,
                    String cardType,
                    String installMonth,
                    String approvedId,
                    String cardMid,
                    String interestFreeInstall,
                    String cardItemCode) {
        this.purchaseCorp = purchaseCorp;
        this.purchaseCorpCode = purchaseCorpCode;
        this.issuerCorp = issuerCorp;
        this.issuerCorpCode = issuerCorpCode;
        this.bin = bin;
        this.cardType = cardType;
        this.installMonth = installMonth;
        this.approvedId = approvedId;
        this.cardMid = cardMid;
        this.interestFreeInstall = interestFreeInstall;
        this.cardItemCode = cardItemCode;
    }
}
