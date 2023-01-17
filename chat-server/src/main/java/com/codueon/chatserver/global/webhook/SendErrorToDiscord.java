package com.codueon.chatserver.global.webhook;

import com.codueon.chatserver.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SendErrorToDiscord {
    private final WebHookFeign webHookFeign;


    public void sendBusinessExceptionToDiscord(BusinessLogicException e) {
        WebHookError webHookError = WebHookError.builder()
                .content(e.getMessage() + " error occurred \n")
                .embeds(makeErrorList(e))
                .tts(false)
                .build();
        webHookFeign.sendServiceLogging("application/json", webHookError);
    }

    public void sendServerExceptionToDiscord(Exception e) {
        WebHookError webHookError = WebHookError.builder()
                .content("server error occurred \n")
                .embeds(makeErrorList(e))
                .tts(false)
                .build();
        webHookFeign.sendServerLogging("application/json", webHookError);
    }

    private List<ServiceError> makeErrorList(Exception... exceptions) {
        return Arrays.stream(exceptions).map(
                e -> ServiceError.builder()
                        .title(e.getMessage())
                        .description(e.toString())
                        .build()).collect(Collectors.toList());
    }
}
