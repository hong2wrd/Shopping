package com.shopping.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepositoryImpl implements RedisRefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_PREFIX = "refresh:";

    @Override
    public void save(String memId, String refreshToken, long ttl) {
        redisTemplate.opsForValue().set(REFRESH_PREFIX + memId, refreshToken, ttl, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<String> findRefreshTokenByMemberId(String memId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(REFRESH_PREFIX + memId));
    }

    @Override
    public void delete(String memId) {
        redisTemplate.delete(REFRESH_PREFIX + memId);
    }
}
