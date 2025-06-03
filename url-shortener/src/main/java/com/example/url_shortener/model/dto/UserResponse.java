package com.example.url_shortener.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String lastname;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
