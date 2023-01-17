package com.codueon.chatserver.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {
    private int status;
    private String message;
    private List<Field> fieldError;
    private List<ConstraintViolation> violationErrors;

    public ErrorResponse(List<Field> fieldError, List<ConstraintViolation> violationErrors) {
        this.fieldError = fieldError;
        this.violationErrors = violationErrors;
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(Set<javax.validation.ConstraintViolation<?>> violations) {
        return new ErrorResponse(null, ConstraintViolation.of(violations));
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(Field.of(bindingResult), null);
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode.getStatus(), exceptionCode.getMessage());
    }


    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.value(), message);
    }

    @Getter
    public static class Field {
        private final String field;
        private final Object rejectedValue;
        private final String reason;

        public Field(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<Field> of(BindingResult bindingResult) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(err -> new Field(
                            err.getField(),
                            err.getRejectedValue() == null ? "" : err.getRejectedValue().toString(),
                            err.getDefaultMessage()
                    )).collect(Collectors.toList());
        }
    }

    @Getter
    public static class ConstraintViolation {
        private final String constraintViolation;
        private final String rejectedValue;
        private final String reason;

        public ConstraintViolation(String constraintViolation, String rejectedValue, String reason) {
            this.constraintViolation = constraintViolation;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<ConstraintViolation> of(Set<javax.validation.ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(err -> new ConstraintViolation(
                            err.getPropertyPath().toString(),
                            err.getInvalidValue().toString(),
                            err.getMessage()
                    )).collect(Collectors.toList());
        }
    }
}
