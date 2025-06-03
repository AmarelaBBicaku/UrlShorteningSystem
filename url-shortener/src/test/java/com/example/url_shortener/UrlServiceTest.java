package com.example.url_shortener;

import com.example.url_shortener.exception.NotFoundException;
import com.example.url_shortener.model.Url;
import com.example.url_shortener.model.User;
import com.example.url_shortener.model.dto.UrlRequest;
import com.example.url_shortener.model.dto.UrlResponse;
import com.example.url_shortener.repository.UrlRepository;
import com.example.url_shortener.repository.UserRepository;
import com.example.url_shortener.serviceimpl.UrlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UrlServiceImpl urlService;

    private UrlRequest urlRequest;
    private User user;
    private Url url;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);

        urlRequest = new UrlRequest();
        urlRequest.setOriginalUrl("https://feut.edu.al/lajmerime");
        urlRequest.setUserId(1L);
        urlRequest.setExpirationTime(10);

        url = Url.builder()
                .originalUrl("https://feut.edu.al/lajmerime")
                .shortUrl("w2gr2")
                .expirationTime(LocalDateTime.now().plusMinutes(10))
                .clickCount(0)
                .user(user)
                .build();
    }

    @Test
    public void testSaveUrl_NewUrl() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(urlRepository.findByOriginalUrlAndUser(any(), any())).thenReturn(Optional.empty());
        when(urlRepository.findByShortUrl(any())).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenReturn(url);

        UrlResponse response = urlService.saveUrl(urlRequest);

        assertNotNull(response);
        assertEquals("https://feut.edu.al/lajmerime", response.getOriginalUrl());
        assertEquals("w2gr2", response.getShortUrl());
        assertEquals(user.getUserId(), response.getUserId());
    }

    @Test
    public void testSaveUrl_ExistingUrl() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(urlRepository.findByOriginalUrlAndUser(any(), any())).thenReturn(Optional.of(url));
        when(urlRepository.save(any())).thenReturn(url);

        UrlResponse response = urlService.saveUrl(urlRequest);

        assertNotNull(response);
        assertEquals("w2gr2", response.getShortUrl());
    }

    @Test
    public void testGetOriginalUrl_Valid() {
        User user = new User();
        user.setUserId(1L);

        Url url = new Url();
        url.setOriginalUrl("https://feut.edu.al/lajmerime");
        url.setShortUrl("w2gr2");
        url.setExpirationTime(LocalDateTime.now().plusMinutes(10));
        url.setClickCount(0);
        url.setUser(user);

        when(urlRepository.findByShortUrl("w2gr2")).thenReturn(Optional.of(url));
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        String result = urlService.getOriginalUrl("w2gr2");

        assertNotNull(result);
        assertEquals("https://feut.edu.al/lajmerime", result);
        assertEquals(1, url.getClickCount());

        verify(urlRepository, times(1)).save(url);
    }

    @Test
    void testGetOriginalUrl_Expired() {
        String shortUrl = "expired123";
        Url expiredUrl = new Url();
        expiredUrl.setShortUrl(shortUrl);
        expiredUrl.setExpirationTime(LocalDateTime.now().minusMinutes(1));

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(expiredUrl));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> urlService.getOriginalUrl(shortUrl));
        assertTrue(exception.getMessage().toLowerCase().contains("expired"));
    }

    @Test
    public void testRefreshExpiration() {
        when(urlRepository.findByShortUrl("w2gr2")).thenReturn(Optional.of(url));
        when(urlRepository.save(any())).thenReturn(url);

        UrlResponse response = urlService.refreshExpiration("w2gr2", 15);

        assertNotNull(response);
        assertEquals("w2gr2", response.getShortUrl());
    }

    @Test
    public void testDeleteExpiredUrls() {
        when(urlRepository.findAllByExpirationTimeBefore(any())).thenReturn(List.of(url));

        urlService.deleteExpiredUrls();

        verify(urlRepository, times(1)).deleteAll(any());
    }
}