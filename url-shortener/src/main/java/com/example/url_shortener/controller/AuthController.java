package com.example.url_shortener.controller;

import com.example.url_shortener.model.dto.AuthRequest;
import com.example.url_shortener.model.dto.AuthResponse;
import com.example.url_shortener.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.authenticateAndGenerateToken(authRequest);
        return ResponseEntity.ok(authResponse);
    }
}
