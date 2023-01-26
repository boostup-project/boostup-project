package com.codueon.boostUp.global.webhook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "webhook-logging", url = "${logging.url}")
public interface WebHookFeign {
    @PostMapping(value = "${logging.server}")
    void sendServerLogging(@RequestHeader("Accept") String contentType,
                           WebHookError webHookError);

    @PostMapping(value = "${logging.service}")
    void sendServiceLogging(@RequestHeader("Accept") String contentType,
                            WebHookError webHookError);
}
