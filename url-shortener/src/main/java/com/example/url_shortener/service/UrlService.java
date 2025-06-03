package com.example.url_shortener.service;

import com.example.url_shortener.model.dto.UrlRequest;
import com.example.url_shortener.model.dto.UrlResponse;

public interface UrlService {
    UrlResponse saveUrl(UrlRequest urlRequest);
    String getOriginalUrl(String shortUrl);
    UrlResponse refreshExpiration(String shortUrl, Integer newExpirationMinutes);
}
