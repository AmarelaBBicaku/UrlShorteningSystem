package com.example.url_shortener.controller;

import com.example.url_shortener.model.dto.UrlRequest;
import com.example.url_shortener.model.dto.UrlResponse;
import com.example.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @PostMapping
    public ResponseEntity<UrlResponse> addUrl(@RequestBody @Valid UrlRequest urlRequest) {
        UrlResponse response = urlService.saveUrl(urlRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortUrl) {
        String originalUrl = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(302).location(URI.create(originalUrl)).build();
    }

    @PutMapping("/{shortUrl}/expiration")
    public ResponseEntity<UrlResponse> refreshExpiration(
            @PathVariable String shortUrl,
            @RequestParam(required = false) Integer expirationMinutes) {
        UrlResponse response = urlService.refreshExpiration(shortUrl, expirationMinutes);
        return ResponseEntity.ok(response);
    }
}

