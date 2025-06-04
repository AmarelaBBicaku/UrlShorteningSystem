package com.example.url_shortener.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UrlRequest {
    private Long userId;
    @NotBlank(message = "Original URL must not be blank")
    private String originalUrl;
    @Positive(message = "Expiration time must be a positive number")
    private Integer expirationTime;
}


