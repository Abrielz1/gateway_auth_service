package com.nemo.gateway_auth_service.app.domain.dto.redis;

import java.util.List;

public record RedisSessionData(String userId, List<String> authorities) {
}
