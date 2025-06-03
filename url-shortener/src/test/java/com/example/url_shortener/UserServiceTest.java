package com.example.url_shortener;

import com.example.url_shortener.model.User;
import com.example.url_shortener.model.dto.UserRequest;
import com.example.url_shortener.model.dto.UserResponse;
import com.example.url_shortener.repository.UserRepository;
import com.example.url_shortener.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setUsername("amarela");
        user.setLastname("bicaku");
        user.setEmail("amarela@gmail.com");
        user.setPassword("encodedPassword");

        userRequest = new UserRequest();
        userRequest.setUsername("amarela");
        userRequest.setLastname("bicaku");
        userRequest.setEmail("amarela@gmail.com");
        userRequest.setPassword("password123");
    }

    @Test
    void createUser_shouldReturnUserResponse() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(userRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("amarela", response.getUsername());
        assertEquals("bicaku", response.getLastname());
        assertEquals("amarela@gmail.com", response.getEmail());

        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }
}