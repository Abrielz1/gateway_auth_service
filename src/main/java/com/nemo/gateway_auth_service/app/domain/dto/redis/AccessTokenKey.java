package com.nemo.gateway_auth_service.app.domain.dto.redis;

import java.util.UUID;

public record AccessTokenKey(String key, UUID userId) {

   public static AccessTokenKey of(UUID userId) {
       return new AccessTokenKey("refresh", userId);
   }
}
