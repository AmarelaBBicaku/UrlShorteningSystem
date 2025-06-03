package com.example.url_shortener.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrlResponse {
        private Long id;
        private String originalUrl;
        private String shortUrl;
        private LocalDateTime expirationTime;
        private int clickCount;
        private Long userId;
    }

