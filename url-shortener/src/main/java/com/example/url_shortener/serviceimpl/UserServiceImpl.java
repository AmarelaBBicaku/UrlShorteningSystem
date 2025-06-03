package com.example.url_shortener.serviceimpl;

import com.example.url_shortener.model.User;
import com.example.url_shortener.model.dto.UserRequest;
import com.example.url_shortener.model.dto.UserResponse;
import com.example.url_shortener.repository.UserRepository;
import com.example.url_shortener.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }
    public UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setLastname(user.getLastname());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }}

