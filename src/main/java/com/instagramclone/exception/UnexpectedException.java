package com.instagramclone.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnexpectedException extends RuntimeException{

    private String errorCode;
    private String message;

    public UnexpectedException(String message,String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
