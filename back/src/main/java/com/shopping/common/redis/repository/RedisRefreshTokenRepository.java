package com.shopping.common.redis.repository;

import java.util.Optional;

public interface RedisRefreshTokenRepository {

    void save(String memId, String refreshToken, long ttl);

    Optional<String> findRefreshTokenByMemberId(String memId);

    void delete(String memId);
}
