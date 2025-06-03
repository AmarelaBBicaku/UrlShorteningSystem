package com.example.url_shortener.model.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
