package com.hans.controllers;

import com.hans.exceptions.HttpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerController {

    public static class Error {
        private String error;

        public Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    @ExceptionHandler(value = { HttpException.class })
    public ResponseEntity<Error> handleHttpException(HttpException exception) {
        return new ResponseEntity<Error>(new Error(exception.getMessage()), exception.getCode());
    }

}
