package com.codueon.boostUp.domain.suggest.feign;

import feign.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class getFeignClientUtils {

    public static String getRequestBody(Response response) {
        if (response.request().body() == null) return "";
        return new String(response.request().body(), StandardCharsets.UTF_8);
    }

    public static String getResponseBody(Response response) {
        if (response.body() == null) return "";

        try (InputStream inputStream = response.body().asInputStream()){
            return IOUtils.toString(inputStream, String.valueOf(StandardCharsets.UTF_8));
        } catch (IOException e) {
            return "";
        }
    }

}
