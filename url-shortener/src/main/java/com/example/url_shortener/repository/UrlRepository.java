package com.example.url_shortener.repository;

import com.example.url_shortener.model.Url;
import com.example.url_shortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    Optional<Url> findByOriginalUrlAndUser(String originalUrl, User user);
    List<Url> findAllByExpirationTimeBefore(LocalDateTime time);

}
