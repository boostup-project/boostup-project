package com.codueon.boostUp.domain.suggest.feign;

import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class FeignErrorConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return ((methodKey, response) -> {
            if (log.isErrorEnabled()) {
                log.error("{} 요청이 실패했습니다. requestUrl: {}, requestBody: {}, responseBody: {}",
                        methodKey,
                        response.request().url(),
                        getFeignClientUtils.getRequestBody(response),
                        getFeignClientUtils.getResponseBody(response)
                );
            }
            return new BusinessLogicException(ExceptionCode.PAYMENT_URL_REQUEST_FAILED);
        });
    }
}
