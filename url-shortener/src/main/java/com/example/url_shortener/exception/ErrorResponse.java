package com.example.url_shortener.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.ConnectException;
import java.time.Instant;


@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse{
    private Instant timestamp;
    private HttpStatus status;
    private String code;
    private String message;

    @JsonIgnore
    private Throwable exception;

    private static final Logger logger = LoggerFactory.getLogger(ErrorResponse.class);

    public static ResponseEntity<ErrorResponse> from(String message, Exception e) {
        HttpStatus status = resolveHttpStatus(e);

        logger.error("Exception handled: {}", e.getMessage(), e);

        String fullMessage = buildMessage(message, e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status)
                .code(String.valueOf(status.value()))
                .message(fullMessage)
                .exception(e)
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }

    private static HttpStatus resolveHttpStatus(Exception e) {
        if (e instanceof BadRequestException || e instanceof JsonMappingException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof org.springframework.security.core.AuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (e instanceof ForbiddenException) {
            return HttpStatus.FORBIDDEN;
        } else if (e instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (e instanceof ConnectException) {
            return HttpStatus.GATEWAY_TIMEOUT;
        } else if (e instanceof IOException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private static String buildMessage(String baseMessage, Exception e) {
        if (e.getMessage() == null || baseMessage.contains(e.getMessage())) {
            return baseMessage;
        } else {
            return baseMessage + ": " + e.getMessage();
        }
    }
}