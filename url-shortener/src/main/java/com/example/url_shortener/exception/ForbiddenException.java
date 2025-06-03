package com.example.url_shortener.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {
    private final HttpStatus status;

    public ForbiddenException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }


    public HttpStatus getStatus() {
        return status;
    }
}
