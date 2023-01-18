package com.codueon.boostUp.domain.suggest.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message<T> {
    private T data;
    private String message;

    @Builder
    public Message(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
