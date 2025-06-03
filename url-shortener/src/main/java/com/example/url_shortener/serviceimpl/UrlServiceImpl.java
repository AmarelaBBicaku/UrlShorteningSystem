package com.example.url_shortener.serviceimpl;

import com.example.url_shortener.exception.InternalServerErrorException;
import com.example.url_shortener.exception.NotFoundException;
import com.example.url_shortener.model.Url;
import com.example.url_shortener.model.User;
import com.example.url_shortener.model.dto.UrlRequest;
import com.example.url_shortener.model.dto.UrlResponse;
import com.example.url_shortener.repository.UrlRepository;
import com.example.url_shortener.repository.UserRepository;
import com.example.url_shortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService {
    @Autowired
    private  UrlRepository urlRepository;
    @Autowired
    private  UserRepository userRepository;
    private static final int SHORT_CODE_LENGTH = 8;
    private static final int DEFAULT_EXPIRATION_MINUTES = 5;

    @Override
    public UrlResponse saveUrl(UrlRequest urlRequest) {


        User user = userRepository.findById(urlRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + urlRequest.getUserId()));
        Optional<Url> existingUrlOpt = urlRepository
                .findByOriginalUrlAndUser(urlRequest.getOriginalUrl(), user)
                .filter(url -> url.getExpirationTime().isAfter(LocalDateTime.now()));

        int expirationMinutes = (urlRequest.getExpirationTime() != null && urlRequest.getExpirationTime() > 0)
                ? urlRequest.getExpirationTime()
                : DEFAULT_EXPIRATION_MINUTES;

        if (existingUrlOpt.isPresent()) {
            Url existingUrl = existingUrlOpt.get();
            existingUrl.setExpirationTime(LocalDateTime.now().plusMinutes(expirationMinutes));
            Url updatedUrl = urlRepository.save(existingUrl);
            return mapToResponse(updatedUrl);
        }

        String shortCode = generateUniqueShortCode();

        LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(expirationMinutes);

        Url newUrl = Url.builder()
                .originalUrl(urlRequest.getOriginalUrl())
                .shortUrl(shortCode)
                .expirationTime(expirationDateTime)
                .clickCount(0)
                .user(user)
                .build();

        try {
            Url savedUrl = urlRepository.save(newUrl);
            return mapToResponse(savedUrl);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to save URL: " + e.getMessage());
        }
    }

    private String generateUniqueShortCode() {
        String candidate;
        do {
            candidate = generateRandomAlphaNumeric(SHORT_CODE_LENGTH);
        } while (urlRepository.findByShortUrl(candidate).isPresent());
        return candidate;
    }

    private String generateRandomAlphaNumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Override
    public String getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new NotFoundException("Short URL not found  " + shortUrl));

        if (url.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new NotFoundException("Short URL has expired: " + shortUrl);
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url.getOriginalUrl();
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void deleteExpiredUrls() {
        LocalDateTime now = LocalDateTime.now();
        List<Url> expiredUrls = urlRepository.findAllByExpirationTimeBefore(now);

        if (!expiredUrls.isEmpty()) {
            urlRepository.deleteAll(expiredUrls);
        }
    }
    @Override
    public UrlResponse refreshExpiration(String shortUrl, Integer newExpirationMinutes) {
        int expirationMinutes = (newExpirationMinutes != null && newExpirationMinutes > 0)
                ? newExpirationMinutes
                : DEFAULT_EXPIRATION_MINUTES;

        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new NotFoundException("Short URL not found: " + shortUrl,HttpStatus.NOT_FOUND));

        url.setExpirationTime(LocalDateTime.now().plusMinutes(expirationMinutes));
        Url updatedUrl = urlRepository.save(url);

        return mapToResponse(updatedUrl);
    }
    private UrlResponse mapToResponse(Url url) {
        return new UrlResponse(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getExpirationTime(),
                url.getClickCount(),
                url.getUser().getUserId()
        );
    }
}