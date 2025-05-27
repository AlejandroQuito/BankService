package com.pioneerPixel.BankService.service;

import com.pioneerPixel.BankService.security.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshJwtService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SecurityConstants securityConstants;

    public void save(String token, Long userId) {
        String key = buildKey(token);
        Duration ttl = Duration.ofMillis(securityConstants.getRefreshLifetime());

        redisTemplate.opsForValue().set(key, userId.toString(), ttl);
    }

    public boolean existsByToken(String token) {
        String key = buildKey(token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void deleteByToken(String token) {
        redisTemplate.delete(buildKey(token));
    }

    private String buildKey(String token) {
        return "refresh: " + token;
    }
}
