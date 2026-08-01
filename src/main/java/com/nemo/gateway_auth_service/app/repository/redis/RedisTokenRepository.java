package com.nemo.gateway_auth_service.app.repository.redis;

import java.util.UUID;

public interface RedisTokenRepository {

    void saveTokens(UUID userId, String accessToken, String refreshToken);

    void logout(UUID userId);
}
