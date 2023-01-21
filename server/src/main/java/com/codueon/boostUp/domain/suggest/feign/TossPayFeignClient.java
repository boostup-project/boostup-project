package com.codueon.boostUp.domain.suggest.feign;

import com.codueon.boostUp.domain.suggest.toss.*;
import com.codueon.boostUp.domain.suggest.utils.PayConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "tosspay", url = "https://api.tosspayments.com", configuration = {FeignErrorConfig.class})
public interface TossPayFeignClient {
    @PostMapping(value = "/v1/payments", consumes = "application/json")
    TossPayReadyInfo readyForTossPayment(@RequestHeader(PayConstants.AUTHORIZATION) String authorization,
                                         @RequestHeader(value = "Content-Type") String contentType,
                                         @RequestBody ReadyToTossPayInfo body);

    @PostMapping(value = "/v1/payments/confirm", consumes = "application/json")
    TossPaySuccessInfo successForPayment(@RequestHeader(PayConstants.AUTHORIZATION) String authorization,
                                         @RequestHeader(value = "Content-Type") String contentType,
                                         @RequestBody RequestForTossPayInfo body);

    @PostMapping(value = "/v1/payments/{paymentKey}/cancel", consumes = "application/json")
    TossPayCancelInfo cancelPayment(@RequestHeader(PayConstants.AUTHORIZATION) String authorization,
                                    @RequestHeader(value = "Content-Type") String contentType,
                                    @PathVariable("paymentKey") String paymentKey,
                                    @RequestBody RequestForTossPayCancelInfo body);
}
