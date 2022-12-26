package com.codueon.boostUp.global.exception;

import com.codueon.boostUp.global.webhook.SendErrorToDiscord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalAdvice {
    private final SendErrorToDiscord sendErrorToDiscord;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        sendErrorToDiscord.sendServerExceptionToDiscord(e);
        return ErrorResponse.of(e.getBindingResult());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        sendErrorToDiscord.sendServerExceptionToDiscord(e);
        return ErrorResponse.of(e.getConstraintViolations());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleBusinessLogicException(BusinessLogicException e) {
        sendErrorToDiscord.sendBusinessExceptionToDiscord(e);
        final ErrorResponse response = ErrorResponse.of(e.getExceptionCode());
        return ResponseEntity.status(e.getExceptionCode().getStatus()).body(response);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        sendErrorToDiscord.sendServerExceptionToDiscord(e);
        return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, "메서드 문법이 틀렸습니다. 문법을 지켜주세요.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        sendErrorToDiscord.sendServerExceptionToDiscord(e);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "정확한 제이슨 요청을 부탁드립니다.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        sendErrorToDiscord.sendServerExceptionToDiscord(e);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "파라미터가 유효하지 않습니다.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNullPointException(NullPointerException e) {
        sendErrorToDiscord.sendServerExceptionToDiscord(e);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "500 Error");
    }
}
