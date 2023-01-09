package com.instagramclone.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Getter
public class APIException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public APIException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}