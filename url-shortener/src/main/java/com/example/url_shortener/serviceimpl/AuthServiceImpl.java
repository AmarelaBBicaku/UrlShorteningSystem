package com.example.url_shortener.serviceimpl;

import com.example.url_shortener.model.dto.AuthRequest;
import com.example.url_shortener.model.dto.AuthResponse;
import com.example.url_shortener.security.JwtUtil;
import com.example.url_shortener.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public AuthResponse authenticateAndGenerateToken(AuthRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String username = authentication.getName();
            String accessToken = jwtUtil.generateToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);

            return new AuthResponse(accessToken, refreshToken);

        } catch (Exception ex) {
            throw new RuntimeException("Authentication failed: " + ex.getMessage());
        }
    }}

