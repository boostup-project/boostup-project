package com.codueon.boostUp.domain.chat.utils;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;

public class StompFrameHandlerImpl<T> implements StompFrameHandler {

    private final T response;
    private final BlockingQueue<T> responses;

    public StompFrameHandlerImpl(T response, BlockingQueue<T> responses) {
        this.response = response;
        this.responses = responses;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return response.getClass();
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        responses.offer((T) payload);
    }
}
