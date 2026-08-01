package com.nemo.gateway_auth_service.app.domain.dto.redis;

import java.util.UUID;

public record RefreshTokenKey(String type, UUID userId) {

    public static RefreshTokenKey of(UUID userId) {
        return new RefreshTokenKey("acces", userId);
    }
}
