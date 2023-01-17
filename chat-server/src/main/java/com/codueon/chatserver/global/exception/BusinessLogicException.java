package com.codueon.chatserver.global.exception;

import lombok.Getter;

public class BusinessLogicException extends RuntimeException{

    @Getter
    public ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
