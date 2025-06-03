package com.example.url_shortener.service;

import com.example.url_shortener.model.dto.AuthRequest;
import com.example.url_shortener.model.dto.AuthResponse;

public interface AuthService {
    AuthResponse authenticateAndGenerateToken(AuthRequest loginRequest);
}
