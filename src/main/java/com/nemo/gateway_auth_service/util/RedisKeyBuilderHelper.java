package com.nemo.gateway_auth_service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;
import com.nemo.gateway_auth_service.app.security.configuration.RedisKeysProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisKeyBuilderHelper {

    private final RedisKeysProperties redisKeys;

    public String buildKey(final String fingerprintHash) {
        return redisKeys.getKeys().getWhitelist().getFingerprintKeyFormat().replace("{fingerprint}", fingerprintHash);
    }

    // тупые собиралки ключа для Redis
    public String buildAccessKey(final String accessToken) {
        return redisKeys.getKeys().getBlacklist().getAccessTokenPrefix() + accessToken;
    }

    public String buildRefreshKey(final String refreshToken) {
        return redisKeys.getKeys().getBlacklist().getRefreshTokenPrefix() + refreshToken;
    }

    // "Тупой" собиральщик ключа из префикса (из YAML) и ID
    public String buildKeyBySessionId(final UUID sessionId) {
        return this.redisKeys.getKeys().getActiveSessionPrefix() + sessionId.toString();
    }
}
