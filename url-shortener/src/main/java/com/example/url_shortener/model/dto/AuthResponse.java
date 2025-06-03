package com.example.url_shortener.model.dto;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String jwtToken;
    private String refreshToken;
}
