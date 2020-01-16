package com.hans.exceptions;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
    private String message;
    private HttpStatus code;

    public HttpException(String message, HttpStatus code) {
        super(String.format("%s - %d", message, code.value()));
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getCode() {
        return code;
    }
}
