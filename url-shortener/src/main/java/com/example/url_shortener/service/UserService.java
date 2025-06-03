package com.example.url_shortener.service;


import com.example.url_shortener.model.dto.UserRequest;
import com.example.url_shortener.model.dto.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
}
