package com.nemo.gateway_auth_service.app.service.orchestration.worker;

import java.util.UUID;

public interface ClientSessionWorker {

    void saveKeysToRedis(UUID userUUID, String accessTokenKey, String refreshTokenKey);

    String getRefreshTokenFromRedis(UUID userId);

    void deleteSession(UUID userId);
}
