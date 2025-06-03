package com.example.url_shortener.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    private final HttpStatus status;

    public BadRequestException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }


    public HttpStatus getStatus() {
        return status;
    }
}

